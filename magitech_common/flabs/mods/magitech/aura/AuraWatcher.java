package flabs.mods.magitech.aura;

import java.util.ArrayList;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class AuraWatcher {

	ArrayList<AuraChunkData> loadedChunks = new ArrayList<AuraChunkData>();
	AuraSavedData save;

	@ForgeSubscribe
	public void onChunkLoad(ChunkEvent.Load load) {
		// System.out.println(load);
		AuraChunkData ac = getData(load.getChunk());
		if (ac != null) {
			ac.loaded = true;
		} else {
			loadedChunks.add(new AuraChunkData(load.getChunk()));
		}
	}

	@ForgeSubscribe
	public void onChunkUnload(ChunkEvent.Unload unload) {
		// System.out.println(unload);
		AuraChunkData ac = getData(unload.getChunk());
		if (ac != null) {
			ac.loaded = false;
		} else {
			ac = new AuraChunkData(unload.getChunk());
			ac.loaded = false;
			loadedChunks.add(ac);
		}
	}

	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load load) {
		save = (AuraSavedData) load.world.loadItemData(AuraSavedData.class, AuraSavedData.nbtName);
		if (save == null) {
			save = new AuraSavedData(AuraSavedData.nbtName);
			load.world.setItemData(AuraSavedData.nbtName, save);
		}
	}

	/**
	 * Returns the Data if this Chunk is in the List
	 */
	private AuraChunkData getData(Chunk c) {
		for (AuraChunkData ac : loadedChunks) {
			if (c.xPosition == ac.chunkX && c.zPosition == ac.chunkZ) {
				return ac;
			}
		}

		return null;
	}
}
