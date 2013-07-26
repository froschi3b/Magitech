package flabs.mods.magitech.client;

import java.awt.Color;

import flabs.mods.magitech.*;
import flabs.mods.magitech.item.Wand;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class GuiListenchanger {
    ResourceLocation res = new ResourceLocation(Version.guiTex + "magigui.png");

	@ForgeSubscribe
	public void drawGUI(RenderGameOverlayEvent.Post post) {
		if (post.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
			ItemStack equip = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
			if (equip != null && equip.getItem() instanceof Wand) {
				Minecraft.getMinecraft().renderEngine.func_110581_b(res);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 64, 16);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 80, 0, 16, 16);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 16, 0, 0, 64, 16);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 16, 64, 0, 16, 16);
				Minecraft.getMinecraft().fontRenderer.drawString( "oha", 24, 4, Color.WHITE.getRGB());
			}
		}
	}
}
