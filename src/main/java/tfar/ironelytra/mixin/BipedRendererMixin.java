package tfar.ironelytra.mixin;

import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ironelytra.IronElytraLayer;

@Mixin(ArmorStandRenderer.class)
abstract class BipedRendererMixin extends LivingRenderer {

	public BipedRendererMixin(EntityRendererManager rendererManager, EntityModel entityModelIn, float shadowSizeIn) {
		super(rendererManager, entityModelIn, shadowSizeIn);
	}

	@Inject(method = "<init>",at = @At("RETURN"))
	private void injectElytraLayer(EntityRendererManager manager, CallbackInfo ci) {
		this.addLayer(new IronElytraLayer(this));
	}
}
