package tfar.ironelytra.mixin;

import net.minecraft.network.play.ServerPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetHandler.class)
public class ServerPlayNetHandlerMixin {

	@ModifyConstant(method = "processPlayer",constant = @Constant(floatValue = 300))
	private float speedCap(float old){
		return 3200;
	}
}
