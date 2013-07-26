package flabs.mods.magitech.client;

import org.lwjgl.opengl.GL11;

import flabs.mods.magitech.block.TileEntityMagiCrucible;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;

public class RenderMagiCrucibleTile extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float n) {
		TileEntityMagiCrucible crucible = (TileEntityMagiCrucible) tileentity;
		if (crucible.showamount <= 0) {
			return;
		}
		x += 0.5;
		y -= 0.5;
		z += 0.5;

		String s = EnumChatFormatting.AQUA + (crucible.getTankInfo(null)[0].fluid == null ? "0" : ((Integer) crucible.getTankInfo(null)[0].fluid.amount).toString());

		FontRenderer fontrenderer = this.tileEntityRenderer.getFontRenderer();
		float f = 1.6F;
		float f1 = 0.016666668F * f;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.0F, (float) y + 2F, (float) z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		byte b0 = 0;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		int j = fontrenderer.getStringWidth(s) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex((double) (-j - 1), (double) (-1 + b0), 0.0D);
		tessellator.addVertex((double) (-j - 1), (double) (8 + b0), 0.0D);
		tessellator.addVertex((double) (j + 1), (double) (8 + b0), 0.0D);
		tessellator.addVertex((double) (j + 1), (double) (-1 + b0), 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, 553648127);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();

	}
}
