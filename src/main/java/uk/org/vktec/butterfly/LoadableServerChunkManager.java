// vim: noet

package uk.org.vktec.butterfly;

import net.minecraft.util.math.ChunkPos;

public interface LoadableServerChunkManager {
	public void forceLoadChunk(ChunkPos pos);
}
