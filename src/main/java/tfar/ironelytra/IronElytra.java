package tfar.ironelytra;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;
import top.theillusivec4.caelus.api.RenderElytraEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(IronElytra.MODID)
public class IronElytra {
	// Directly reference a log4j logger.

	public static final String MODID = "ironelytra";

	public static Enchantment HARD_HEAD;

	private static final EquipmentSlotType[] ARMOR_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

	public static boolean curiousElytraLoaded;


	public IronElytra() {
		// Register the setup method for modloading
		curiousElytraLoaded = ModList.get().isLoaded("curiouselytra");
		IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		iEventBus.addGenericListener(Item.class, this::items);
		iEventBus.addGenericListener(Enchantment.class, this::enchant);
		iEventBus.addListener(this::enqueue);
		EVENT_BUS.addListener(this::damage);
		if (FMLEnvironment.dist == Dist.CLIENT && curiousElytraLoaded) {
			EVENT_BUS.addListener(this::renderElytra);
		}
	}

	private void enqueue(InterModEnqueueEvent evt) {
		if (curiousElytraLoaded)
		InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.BACK.getMessageBuilder().build());
	}

	private void renderElytra(RenderElytraEvent evt) {
		PlayerEntity playerEntity = evt.getPlayer();
		CuriosApi.getCuriosHelper().getCuriosHandler(playerEntity).ifPresent((handler) -> {
			Set<String> tags = CuriosApi.getCuriosHelper().getCurioTags(Items.ELYTRA);

			for (String id : tags) {
				handler.getStacksHandler(id).ifPresent((stacksHandler) -> {
					IDynamicStackHandler stackHandler = stacksHandler.getStacks();

					for (int i = 0; i < stackHandler.getSlots(); ++i) {
						ItemStack stack = stackHandler.getStackInSlot(i);
						if (stack.getItem() instanceof IronElytraItem && stacksHandler.getRenders().get(i)) {
							evt.setRender(true);
							evt.setResourceLocation(((IronElytraItem)stack.getItem()).elytraProperties.texture);
							if (stack.isEnchanted()) {
								evt.setEnchanted(true);
							}
						}
					}
				});
			}
		});
	}

	public static int base = Items.ELYTRA.getMaxDamage();

	private void items(final RegistryEvent.Register<Item> event) {
		register(new IronElytraItem(new Item.Properties().maxDamage(base * 2)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON), new ElytraProperties(.004,.02,.004,
						new ResourceLocation(MODID, "textures/entity/iron_elytra.png"))), "iron_elytra", event.getRegistry());

		register(new IronElytraItem(new Item.Properties().maxDamage(base * 3)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON), new ElytraProperties(.003,.02,.003,
						new ResourceLocation(MODID, "textures/entity/gold_elytra.png"))), "gold_elytra", event.getRegistry());

		register(new IronElytraItem(new Item.Properties().maxDamage(base * 4)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON), new ElytraProperties(.002,.02,.002,
						new ResourceLocation(MODID, "textures/entity/diamond_elytra.png"))), "diamond_elytra", event.getRegistry());

		register(new IronElytraItem(new Item.Properties().maxDamage(base * 5)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON).isBurnable(), new ElytraProperties(.001,.02,.001,
						new ResourceLocation(MODID, "textures/entity/netherite_elytra.png"))), "netherite_elytra", event.getRegistry());

		register(new ElectricBoosterItem(new Item.Properties().group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON)), "electric_booster", event.getRegistry());

	}

	private void enchant(final RegistryEvent.Register<Enchantment> event) {
		HARD_HEAD = register(new HardHeadEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS), "hard_head", event.getRegistry());
	}

	private void damage(LivingHurtEvent e) {
		if (e.getSource() == DamageSource.FLY_INTO_WALL) {
			int reduction = EnchantmentHelper.getMaxEnchantmentLevel(HARD_HEAD, e.getEntityLiving());
			e.setAmount(e.getAmount() / (reduction + 1));
		}
	}

	private static <T extends IForgeRegistryEntry<T>> T register(T obj, String name, IForgeRegistry<T> registry) {
		registry.register(obj.setRegistryName(new ResourceLocation(MODID, name)));
		return obj;
	}

	public static ICapabilityProvider createProvider(IEnergyStorage energyStorage) {
		return new Provider(energyStorage);
	}

	public static class Provider implements ICapabilityProvider {
		final LazyOptional<IEnergyStorage> capability;

		Provider(IEnergyStorage energyStorage) {
			this.capability = LazyOptional.of(() -> energyStorage);
		}

		@Nonnull
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			return CapabilityEnergy.ENERGY.orEmpty(cap, this.capability);
		}
	}

	public static final Config.Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;

	static {
		final Pair<Config.Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Config.Server::new);
		SERVER_SPEC = specPair2.getRight();
		SERVER = specPair2.getLeft();
	}

	public static class Config {

		public static class Server {

			public static ForgeConfigSpec.IntValue booster_capacity;

			public Server(ForgeConfigSpec.Builder builder) {
				booster_capacity = builder.comment("FE capacity of electric booster").defineInRange("booster_capacity",1000000,0, Integer.MAX_VALUE);
			}
		}
	}
}
