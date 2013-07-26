package flabs.mods.magitech.aura;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class AuraSavedData extends WorldSavedData{
	public static final String nbtName="MagiAura";

	public AuraSavedData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		System.out.println("Reading Aura Data");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		System.out.println("Writing Aura Data");
	}

}
