package tfar.ironelytras.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import tfar.ironelytras.IronElytraItem;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow public abstract ItemStack getItemStackFromSlot(EquipmentSlotType slotIn);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Redirect(method = "func_226566_ei_",at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item fakeElytra(ItemStack stack) {
		if (stack.getItem() instanceof IronElytraItem)return Items.ELYTRA;
		return stack.getItem();
	}
}
