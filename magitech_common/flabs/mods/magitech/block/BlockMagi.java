package flabs.mods.magitech.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flabs.mods.magitech.Magitech;
import flabs.mods.magitech.client.MagiColor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockMagi extends BlockFluidClassic implements ITileEntityProvider {
    
    Icon[] icons = new Icon[2];
    
    public BlockMagi(int id, Fluid fluid) {
        super(id, fluid, Material.water);
    }

    @Override
    public void registerIcons(IconRegister ir) {
        icons[0]=ir.registerIcon("magitech:magi_still");
        icons[1]=ir.registerIcon("magitech:magi_flow");
        Magitech.instance.fluidmagi.setIcons(icons[0],icons[1]);
    }
    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta) {
        return side!=0 || side!=1 ? icons[1] : icons[0];
    }
    @SideOnly(Side.CLIENT)

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess world, int x, int y, int z){
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityMagi) {
            return MagiColor.getColor(((TileEntityMagi) te).percentage);
        }
        return super.colorMultiplier(world, x, y, z);
    }
    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityMagi();
    }
    
    @ForgeSubscribe
    /**
     * Called by ItemBucket when it tries to fill a bucket
     * @param evt
     */
    public void bucketFill(FillBucketEvent evt) {
        if (evt.current.getItem().equals(Item.bucketEmpty) && evt.target.typeOfHit == EnumMovingObjectType.TILE) {
            if (evt.world.canMineBlock(evt.entityPlayer, evt.target.blockX, evt.target.blockY, evt.target.blockZ)) {
                if (evt.entityPlayer.canPlayerEdit(evt.target.blockX, evt.target.blockY, evt.target.blockZ, evt.target.sideHit, evt.current)) {
                    if (evt.world.getBlockId(evt.target.blockX, evt.target.blockY, evt.target.blockZ) == blockID) {
                        evt.world.setBlockToAir(evt.target.blockX, evt.target.blockY, evt.target.blockZ);
                        evt.result = new ItemStack(Magitech.instance.magibucket);
                        evt.setResult(Result.ALLOW);
                    }
                }
            }
        }
    }
}
