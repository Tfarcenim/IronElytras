package tfar.ironelytras.mixin;

import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraItem.class)
public class ElytraItemMixin {
	@Inject(method = "isUsable",at = @At("HEAD"),cancellable = true)
	private static void isRepairable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
	}
}
