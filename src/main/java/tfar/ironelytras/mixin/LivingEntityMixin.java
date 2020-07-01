package tfar.ironelytras.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tfar.ironelytras.IronElytraItem;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

	@Shadow public abstract ItemStack getItemStackFromSlot(EquipmentSlotType slotIn);

	protected LivingEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Redirect(method = "updateElytra",at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item fakeElytra(ItemStack stack){
		if (stack.getItem() instanceof IronElytraItem)return Items.ELYTRA;
		return stack.getItem();
	}

	@ModifyArgs(method = "travel",at = @At(value = "INVOKE",target = "Lnet/minecraft/util/math/Vec3d;mul(DDD)Lnet/minecraft/util/math/Vec3d;"),
					slice = @Slice(to = @At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;move(Lnet/minecraft/entity/MoverType;Lnet/minecraft/util/math/Vec3d;)V")))
	private void tweakDrag(Args args){
		ItemStack stack = getItemStackFromSlot(EquipmentSlotType.CHEST);
		if (stack.getItem() instanceof IronElytraItem) {
			IronElytraItem ironElytra = (IronElytraItem)stack.getItem();
			args.setAll(1 - ironElytra.getDrag(), 1 - ironElytra.getDrag(),1 - ironElytra.getDrag());

		}
	}
}
