package tfar.ironelytra;

import net.minecraft.util.ResourceLocation;

public class ElytraProperties {

	public final double dragX;
	public final double dragY;
	public final double dragZ;

	public final ResourceLocation texture;

	public static final ElytraProperties VANILLA = new ElytraProperties(.01,.02,.01, new ResourceLocation("textures/entity/elytra.png"));

	public ElytraProperties(double dragX,double dragY,double dragZ, ResourceLocation texture) {
		this.dragX = dragX;
		this.dragY = dragY;
		this.dragZ = dragZ;
		this.texture = texture;
	}
}
