package tfar.ironelytra;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

public class ItemStackEnergyStorage extends EnergyStorage {

	protected final ItemStack stack;
	public ItemStackEnergyStorage(int capacity, ItemStack stack) {
		super(capacity,capacity,capacity,0);
		this.stack = stack;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int extract = super.extractEnergy(maxExtract, simulate);
		if (extract > 0)markDirty();
		return extract;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int receive = super.receiveEnergy(maxReceive, simulate);
		if (receive > 0)markDirty();
		return receive;
	}

	public void markDirty() {
		stack.getOrCreateTag().putInt("energy",super.energy);
		stack.getOrCreateTag().putInt("capacity",super.capacity);
	}
}
