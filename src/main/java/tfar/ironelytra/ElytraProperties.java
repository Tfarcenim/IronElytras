package tfar.ironelytra;

import net.minecraft.util.ResourceLocation;

public class ElytraProperties {

	public final double drag;
	public final ResourceLocation texture;

	public static final ElytraProperties VANILLA = new ElytraProperties(.01, new ResourceLocation("textures/entity/elytra.png"));

	public ElytraProperties(double drag, ResourceLocation texture) {
		this.drag = drag;
		this.texture = texture;
	}
}
