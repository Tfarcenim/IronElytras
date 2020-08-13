package tfar.ironelytra.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tfar.ironelytra.IronElytraItem;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

	@Shadow public abstract ItemStack getItemStackFromSlot(EquipmentSlotType slotIn);

	protected LivingEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@ModifyArgs(method = "travel",at = @At(value = "INVOKE",target = "Lnet/minecraft/util/math/vector/Vector3d;mul(DDD)Lnet/minecraft/util/math/vector/Vector3d;"),
					slice = @Slice(
									from = @At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;hasNoGravity()Z")))
	private void tweakDrag(Args args){
		ItemStack stack = getItemStackFromSlot(EquipmentSlotType.CHEST);
		if (stack.getItem() instanceof IronElytraItem) {
			IronElytraItem ironElytra = (IronElytraItem)stack.getItem();
			args.setAll(1 - ironElytra.getDragX(), 1 - 2 * ironElytra.getDragY(),1 - ironElytra.getDragZ());
		}
	}
}
