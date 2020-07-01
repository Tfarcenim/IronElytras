package tfar.ironelytras.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ironelytras.IronElytraItem;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	@Shadow @Final public ClientPlayNetHandler connection;

	public ClientPlayerEntityMixin(ClientWorld p_i50991_1_, GameProfile p_i50991_2_) {
		super(p_i50991_1_, p_i50991_2_);
	}

	@Inject(method = "livingTick",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;getItemStackFromSlot(Lnet/minecraft/inventory/EquipmentSlotType;)Lnet/minecraft/item/ItemStack;"))
	private void sendFlyPacket(CallbackInfo ci){
 		ItemStack stack = this.getItemStackFromSlot(EquipmentSlotType.CHEST);
		if (stack.getItem() instanceof IronElytraItem && ElytraItem.isUsable(stack) && this.func_226566_ei_()) {
			this.connection.sendPacket(new CEntityActionPacket(this, CEntityActionPacket.Action.START_FALL_FLYING));
		}
	}
}
