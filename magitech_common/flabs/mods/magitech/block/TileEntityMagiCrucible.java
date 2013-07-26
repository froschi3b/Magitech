package flabs.mods.magitech.block;

import java.util.List;

import flabs.mods.magitech.Magitech;
import flabs.mods.magitech.api.MagiRecipes;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMagiCrucible extends TileEntity implements IInventory, IFluidHandler {

	private ItemStack tempstack[] = new ItemStack[2];
	public FluidTank magitank = new FluidTank(null, 1024);
	private String name;
	private int lastmagi = 0;
	public int showamount = 0;

	// /////////////TODO IInventory Stuff
	@Override
	public int getSizeInventory() {
		return tempstack.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return tempstack[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.tempstack[i] != null) {
			ItemStack itemstack;

			if (this.tempstack[i].stackSize <= j) {
				itemstack = this.tempstack[i];
				this.tempstack[i] = null;
				this.onInventoryChanged();
				return itemstack;
			} else {
				itemstack = this.tempstack[i].splitStack(j);

				if (this.tempstack[i].stackSize == 0) {
					this.tempstack[i] = null;
				}

				this.onInventoryChanged();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.tempstack[i] != null) {
			ItemStack itemstack = this.tempstack[i];
			this.tempstack[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.tempstack[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return this.isInvNameLocalized() ? this.name : "container.magicrucible";
	}

	@Override
	public boolean isInvNameLocalized() {
		return this.name != null;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}


    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return MagiRecipes.getMagiForItem(itemstack) != 0;
    }

	// /////////////TODO TileEntity Stuff

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("Fluid")) {
			NBTTagCompound lqdTag = nbt.getCompoundTag("Fluid");
			magitank.setFluid(FluidStack.loadFluidStackFromNBT(lqdTag));
		}
		NBTTagList nbtitems = nbt.getTagList("Items");

		for (int i = 0; i < nbtitems.tagCount(); ++i) {
			NBTTagCompound itemTag = (NBTTagCompound) nbtitems.tagAt(i);
			int j = itemTag.getByte("Slot");
			if (j >= 0 && j < tempstack.length) {
				tempstack[j] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}
		if (nbt.hasKey("CustomName")) {
			name = nbt.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (magitank.getFluid() != null) {
			NBTTagCompound lqdTag = new NBTTagCompound();
			magitank.getFluid().writeToNBT(lqdTag);
			nbt.setCompoundTag("Fluid", lqdTag);
		}
		NBTTagList nbtitems = new NBTTagList();

		for (int i = 0; i < tempstack.length; ++i) {
			if (tempstack[i] != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte) i);
				tempstack[i].writeToNBT(itemTag);
				nbtitems.appendTag(itemTag);
			}
		}

		nbt.setTag("Items", nbtitems);
		if (name != null) {
			nbt.setString("CustomName", name);
		}
	}

	@Override
	public void updateEntity() {

		if (this.showamount > 0) {
			showamount--;
		}

		if (!worldObj.isRemote) {
			if (this.getBlockMetadata() < 2) {
				this.updateContainingBlockInfo();
			}

			int meta = this.getBlockMetadata();
			float off[] = { 0, 0 };

			if (meta == 2) {
				off[1] = -0.5F;
			} else if (meta == 3) {
				off[1] = 0.5F;
			} else if (meta == 4) {
				off[0] = -0.5F;
			} else if (meta == 5) {
				off[0] = 0.5F;
			}

			if (hasSpaceforMagi()) {
				EntityItem entityitem = TileEntityMagiCrucible.getFirstEntityItem(worldObj, xCoord, yCoord + 0.25, zCoord);

				if (entityitem != null) {
					if (TileEntityHopper.func_96114_a(this, entityitem)) {
						this.onInventoryChanged();
					}
				}
			} else {
				for (int i = 0; i < tempstack.length; i++) {
					if (tempstack[i] != null) {
						EntityItem ei = new EntityItem(worldObj, xCoord + 0.5 + off[0], (double) (yCoord), zCoord + 0.5 + off[1], tempstack[i]);

						worldObj.spawnEntityInWorld(ei);
						tempstack[i] = null;
					}
				}
			}

			for (int i = 0; i < tempstack.length; i++) {
				int tempmag = MagiRecipes.getMagiForItem(tempstack[i]);
				if (tempmag > 0) {
					if (tempmag == magitank.fill(new FluidStack(Magitech.instance.fluidmagi, tempmag), false)) {
						magitank.fill(new FluidStack(Magitech.instance.fluidmagi, tempmag), true);
						tempstack[i].stackSize--;
						if (tempstack[i].stackSize <= 0) {
							tempstack[i] = null;
						}
					}
				}
			}
			if (magitank.getFluid() != null) {
				if (lastmagi != magitank.getFluid().amount) {
					worldObj.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType().blockID, 0, magitank.getFluid().amount);
					lastmagi = magitank.getFluid().amount;
				}
			} else {
				if (lastmagi != 0) {
					worldObj.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType().blockID, 0, 0);
					lastmagi = 0;
				}
			}
		}
	}

	private boolean hasSpaceforMagi() {
		if (magitank.getFluid() == null || magitank.getFluid().amount < magitank.getCapacity()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean receiveClientEvent(int par1, int par2) {
		magitank.setFluid(new FluidStack(Magitech.instance.fluidmagi, par2));
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		return true;
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (magitank.getFluid() != null) {
			nbt.setInteger("Magi", magitank.getFluid().amount);
		}
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		if (pkt.customParam1.hasKey("Magi")) {
			magitank.setFluid(new FluidStack(Magitech.instance.fluidmagi, pkt.customParam1.getInteger("Magi")));
		}
	}

	// /////////////TODO Custom Stuff

	public float getLiquidScaled(int max) {
		if (magitank.getFluid() == null) {
			return 0;
		}
		return ((float) (magitank.getFluid().amount * max) / (float) magitank.getCapacity());
	}

	public static EntityItem getFirstEntityItem(World world, double x, double y, double z) {
		List<?> list = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), IEntitySelector.selectAnything);
		return list.size() > 0 ? (EntityItem) list.get(0) : null;
	}

	public void showAmount() {
		this.showamount = 50;
	}

    // /////////////TODO FluidTank Stuff
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return magitank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return magitank.getFluid()==null||magitank.getFluid().getFluid().equals(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{magitank.getInfo()};
    }

}
