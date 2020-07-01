package tfar.ironelytras.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ironelytras.Captures;
import tfar.ironelytras.IronElytraItem;
import tfar.ironelytras.IronElytras;

@Mixin(ElytraLayer.class)
public class ElytraLayerMixin<T extends LivingEntity> {

	@Inject(method = "render",at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/matrix/MatrixStack;push()V"))
	private void captureEntity(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
		Captures.elytraLayerMixinCapture = entitylivingbaseIn;
	}

	@Redirect(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item fakeElytra(ItemStack stack) {
		if (stack.getItem() instanceof IronElytraItem)return Items.ELYTRA;
		return stack.getItem();
	}

	@ModifyVariable(method = "render",at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/matrix/MatrixStack;translate(DDD)V"))
	private ResourceLocation texture(ResourceLocation old){
		ItemStack stack = Captures.elytraLayerMixinCapture.getItemStackFromSlot(EquipmentSlotType.CHEST);
		if (stack.getItem() instanceof IronElytraItem){
			return new ResourceLocation(IronElytras.MODID,"textures/entity/iron_elytra.png");
		}
		return old;
	}
}
