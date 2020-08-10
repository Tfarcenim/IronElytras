package tfar.ironelytra;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class IronElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T,M> {
	public IronElytraLayer(IEntityRenderer<T,M> rendererIn) {
		super(rendererIn);
	}


	/**
	 * Determines if the ElytraLayer should render.
	 * ItemStack and Entity are provided for modder convenience,
	 * For example, using the same ElytraLayer for multiple custom Elytra.
	 *
	 * @param stack  The Elytra ItemStack
	 * @param entity The entity being rendered.
	 * @return If the ElytraLayer should render.
	 */
	public boolean shouldRender(ItemStack stack, T entity) {
		return stack.getItem() instanceof IronElytraItem;
	}

	/**
	 * Gets the texture to use with this ElytraLayer.
	 * This assumes the vanilla Elytra model.
	 *
	 * @param stack  The Elytra ItemStack.
	 * @param entity The entity being rendered.
	 * @return The texture.
	 */



	public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
		return stack.getItem() instanceof IronElytraItem ? ((IronElytraItem)stack.getItem()).elytraProperties.texture : super.getElytraTexture(stack,entity);
	}
}
