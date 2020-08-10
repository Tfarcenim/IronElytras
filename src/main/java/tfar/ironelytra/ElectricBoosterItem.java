package tfar.ironelytra;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class ElectricBoosterItem extends Item {
	public ElectricBoosterItem(Properties properties) {
		super(properties);
	}

	public static int cost = 1000;

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
	 * {@link #onItemUse}.
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isElytraFlying()) {
			ItemStack itemstack = playerIn.getHeldItem(handIn);
			if (!worldIn.isRemote && canUse(playerIn,itemstack)) {
				worldIn.addEntity(new FireworkRocketEntity(worldIn, itemstack, playerIn));
					itemstack.getTag().putInt("energy",itemstack.getTag().getInt("energy") - cost);
			}

			return ActionResult.func_233538_a_(playerIn.getHeldItem(handIn), worldIn.isRemote());
		} else {
			return ActionResult.resultPass(playerIn.getHeldItem(handIn));
		}
	}

	public boolean canUse(PlayerEntity playerEntity, ItemStack stack) {
		return playerEntity.abilities.isCreativeMode || stack.hasTag() && stack.getTag().getInt("energy") >= cost;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return IronElytra.createProvider(new ItemStackEnergyStorage(1000000,stack));
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTag()) {
			int energy = stack.getTag().getInt("energy");
			int capacity = stack.getTag().getInt("capacity");
			String fraction = energy+"/"+capacity+" FE";
			tooltip.add(new StringTextComponent(fraction));
		} else {
			int energy = 0;
			int capacity = 1000000;
			String fraction = energy+"/"+capacity+" FE";
			tooltip.add(new StringTextComponent(fraction));
		}
	}
}
