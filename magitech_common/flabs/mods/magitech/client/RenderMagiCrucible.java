package flabs.mods.magitech.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flabs.mods.magitech.Magitech;
import flabs.mods.magitech.block.BlockMagiCrucible;
import flabs.mods.magitech.block.TileEntityMagiCrucible;

public class RenderMagiCrucible implements ISimpleBlockRenderingHandler{
	
	private int renderId;
	
	public RenderMagiCrucible(int render){
		renderId=render;
	}
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if(!(block instanceof BlockMagiCrucible)){return false;}
		BlockMagiCrucible magicrucible = (BlockMagiCrucible) block;
		renderer.renderStandardBlock(magicrucible, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(magicrucible.getMixedBrightnessForBlock(world, x, y, z));
        float f = 1.0F;
        int l = magicrucible.colorMultiplier(world, x, y, z);
        float f1 = (float)(l >> 16 & 255) / 255.0F;
        float f2 = (float)(l >> 8 & 255) / 255.0F;
        float f3 = (float)(l & 255) / 255.0F;
        float f4;

        if (EntityRenderer.anaglyphEnable)
        {
            float f5 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f5;
            f2 = f4;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        //Icon icon = magicrucible.getBlockTextureFromSide(2);
        float f5 = 0.125F;
        Icon icon3 = BlockMagiCrucible.func_94375_b("crucible_sideinner");
        renderer.renderFaceZPos(magicrucible, (double)((float)x - 1.0F + f5), (double)y, (double)z, icon3);
        renderer.renderFaceZNeg(magicrucible, (double)((float)x + 1.0F - f5), (double)y, (double)z, icon3);
        renderer.renderFaceXNeg(magicrucible, (double)x, (double)y, (double)((float)z - 1.0F + f5), icon3);
        renderer.renderFaceXPos(magicrucible, (double)x, (double)y, (double)((float)z + 1.0F - f5), icon3);
        Icon icon1 = BlockMagiCrucible.func_94375_b("crucible_inner");
        renderer.renderFaceYPos(magicrucible, (double)x, (double)((float)y - 1.0F + 0.25F), (double)z, icon1);
        renderer.renderFaceYNeg(magicrucible, (double)x, (double)((float)y + 1.0F - 0.75F), (double)z, icon1);
        float i1 = ((TileEntityMagiCrucible)world.getBlockTileEntity(x, y, z)).getLiquidScaled(3);

        if (i1 > 0)
        {
            //Icon icon2 = BlockFluid.func_94424_b("water");
            //Icon icon2=((ProxyClient)Magitech.proxy).magiIcon;
        	Icon icon2=Magitech.instance.magi.getBlockTextureFromSide(0);

            if (i1 > 3)
            {
                i1 = 3;
            }

            renderer.renderFaceYPos(magicrucible, (double)x, (double)((float)y - 1.0F + (6.0F + i1 * 3.0F) / 16.0F), (double)z, icon2);
        }

        return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderId;
	}

}
