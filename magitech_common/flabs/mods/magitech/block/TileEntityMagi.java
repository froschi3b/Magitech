package flabs.mods.magitech.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMagi extends TileEntity{
    public byte percentage=0;
    
    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setByte("perc", percentage);
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        percentage = nbt.getByte("perc");
    }
}
