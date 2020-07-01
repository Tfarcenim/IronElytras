package tfar.ironelytras;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(IronElytras.MODID)
public class IronElytras
{
    // Directly reference a log4j logger.

    public static final String MODID = "ironelytras";

    private static final Logger LOGGER = LogManager.getLogger();

    public IronElytras() {
        // Register the setup method for modloading

        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        iEventBus.addListener(this::setup);
        // Register the doClientStuff method for modloading
        iEventBus.addListener(this::doClientStuff);
        iEventBus.addGenericListener(Item.class,this::items);
    }

    public static int base = Items.ELYTRA.getMaxDamage();

    private void items(final RegistryEvent.Register<Item> event) {
        register(new IronElytraItem(new Item.Properties().maxDamage(base * 2)
                .group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON)),"iron_elytra", event.getRegistry());
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    private static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
        registry.register(obj.setRegistryName(new ResourceLocation(MODID, name)));
    }
}
