package flabs.mods.magitech.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import flabs.mods.magitech.ProxyCommon;
import flabs.mods.magitech.block.TileEntityMagiCrucible;

public class ProxyClient extends ProxyCommon{
	
	public int getRenderId(){
		return RenderingRegistry.getNextAvailableRenderId();
	}
	public void registerRendering(int renderId){
		RenderingRegistry.registerBlockHandler(new RenderMagiCrucible(renderId));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagiCrucible.class, new RenderMagiCrucibleTile());
	}
}
