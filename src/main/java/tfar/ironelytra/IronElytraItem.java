package tfar.ironelytra;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curiouselytra.CurioElytra;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static tfar.ironelytra.IronElytra.curiousElytraLoaded;

public class IronElytraItem extends ElytraItem {
	public final ElytraProperties elytraProperties;

	public IronElytraItem(Properties builder, ElytraProperties elytraProperties) {
		super(builder);
		this.elytraProperties = elytraProperties;
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType == EquipmentSlotType.CHEST;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (!curiousElytraLoaded)
		return null;
		final CurioElytra curioElytra = new CurioElytra(stack);
		return new ICapabilityProvider() {
			LazyOptional<ICurio> curio = LazyOptional.of(() -> curioElytra);

			@Nonnull
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
				return CuriosCapability.ITEM.orEmpty(cap, this.curio);
			}
		};
	}

	@Nullable
	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return EquipmentSlotType.CHEST;
	}

	public double getDragX(){
		return elytraProperties.dragX;
	}

	public double getDragY(){
		return elytraProperties.dragY;
	}

	public double getDragZ(){
		return elytraProperties.dragZ;
	}
}
