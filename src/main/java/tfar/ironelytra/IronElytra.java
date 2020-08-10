package tfar.ironelytra;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(IronElytra.MODID)
public class IronElytra {
	// Directly reference a log4j logger.

	public static final String MODID = "ironelytra";

	public static Enchantment HARD_HEAD;

	private static final EquipmentSlotType[] ARMOR_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

	public IronElytra() {
		// Register the setup method for modloading
		IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		iEventBus.addGenericListener(Item.class, this::items);
		iEventBus.addGenericListener(Enchantment.class, this::enchant);
		EVENT_BUS.addListener(this::damage);
	}

	public static int base = Items.ELYTRA.getMaxDamage();

	private void items(final RegistryEvent.Register<Item> event) {
		register(new IronElytraItem(new Item.Properties().maxDamage(base * 2)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON), new ElytraProperties(.005,
						new ResourceLocation(MODID, "textures/entity/iron_elytra.png"))), "iron_elytra", event.getRegistry());

		register(new IronElytraItem(new Item.Properties().maxDamage(base * 3)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON), new ElytraProperties(.004,
						new ResourceLocation(MODID, "textures/entity/gold_elytra.png"))), "gold_elytra", event.getRegistry());

		register(new IronElytraItem(new Item.Properties().maxDamage(base * 4)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON), new ElytraProperties(.003,
						new ResourceLocation(MODID, "textures/entity/diamond_elytra.png"))), "diamond_elytra", event.getRegistry());

		register(new IronElytraItem(new Item.Properties().maxDamage(base * 5)
						.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON), new ElytraProperties(.002,
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
}
