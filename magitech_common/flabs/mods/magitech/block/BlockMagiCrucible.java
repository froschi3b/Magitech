package flabs.mods.magitech.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import flabs.mods.magitech.Magitech;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

public class BlockMagiCrucible extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private Icon inner;
	@SideOnly(Side.CLIENT)
	private Icon top;
	@SideOnly(Side.CLIENT)
	private Icon bottom;
	@SideOnly(Side.CLIENT)
	private Icon sideinner;

	private int renderId;

	public BlockMagiCrucible(int par1, int renderid) {
		super(par1, Material.iron);
		renderId = renderid;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? this.top : (par1 == 0 ? this.bottom : (par1 == par2 ? this.blockIcon : this.sideinner));
	}

	@SideOnly(Side.CLIENT)
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.inner = par1IconRegister.registerIcon("magitech:crucible_inner");
		this.top = par1IconRegister.registerIcon("magitech:crucible_top");
		this.bottom = par1IconRegister.registerIcon("magitech:crucible_bottom");
		this.blockIcon = par1IconRegister.registerIcon("magitech:crucible_side");
		this.sideinner = par1IconRegister.registerIcon("magitech:crucible_sideinner");
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add
	 * boxes to the list if they intersect the mask.) Parameters: World, X, Y,
	 * Z, mask, list, colliding entity
	 */
	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, @SuppressWarnings("rawtypes") List par6List, Entity par7Entity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		float f = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBoundsForItemRender();
	}

	@SideOnly(Side.CLIENT)
	public static Icon func_94375_b(String par0Str) {
		return par0Str == "crucible_inner" ? Magitech.instance.magicrucible.inner : (par0Str == "crucible_bottom" ? Magitech.instance.magicrucible.bottom : (par0Str == "crucible_sideinner" ? Magitech.instance.magicrucible.sideinner : null));
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return renderId;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public String getItemIconName() {
		return "magitech:crucible";
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer ep, int side, float par7, float par8, float par9) {
		TileEntityMagiCrucible mag = (TileEntityMagiCrucible) world.getBlockTileEntity(x, y, z);
		ItemStack itemstack = ep.inventory.getCurrentItem();

		if (world.isRemote) {
			if (!FluidContainerRegistry.isEmptyContainer(itemstack)) {
				mag.showAmount();
			}
			return true;
		} else {
			if (FluidContainerRegistry.isEmptyContainer(itemstack)) {
				// TODO - Fill Container
				for (FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
					if (data.emptyContainer != null && itemstack.isItemEqual(data.emptyContainer)) {
						if (data.fluid != null && data.fluid.isFluidEqual(mag.getTankInfo(null)[0].fluid)) {
							if (mag.getTankInfo(null)[0].fluid.amount >= data.fluid.amount) {

								ItemStack result = data.filledContainer.copy();

								mag.getTankInfo(null)[0].fluid.amount -= data.fluid.amount;

								if (--itemstack.stackSize <= 0) {
									ep.inventory.mainInventory[ep.inventory.currentItem] = result;
									return true;
								}

								if (!ep.inventory.addItemStackToInventory(result)) {
									ep.dropPlayerItem(result);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMagiCrucible();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		int l = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
		}

		if (l == 1) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
		}

		if (l == 2) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
		}

		if (l == 3) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
		}

		if (par6ItemStack.hasDisplayName()) {
			((TileEntityMagiCrucible) par1World.getBlockTileEntity(par2, par3, par4)).setName(par6ItemStack.getDisplayName());
		}
	}
}
