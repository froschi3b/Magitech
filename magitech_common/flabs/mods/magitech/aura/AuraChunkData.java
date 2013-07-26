package flabs.mods.magitech.aura;

import net.minecraft.world.chunk.Chunk;

public class AuraChunkData {
	public final int chunkX;
	public final int chunkZ;
	public int ender;
	public int magi;
	public boolean loaded;
	
	public AuraChunkData(Chunk chunk){
		chunkX=chunk.xPosition;
		chunkZ=chunk.zPosition;
		loaded=true;
	}
}
