package tfar.ironelytras;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class IronElytraItem extends ElytraItem {
	private final ElytraProperties elytraProperties;

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
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return EquipmentSlotType.CHEST;
	}

	public double getDrag(){
		return elytraProperties.drag;
	}
}
