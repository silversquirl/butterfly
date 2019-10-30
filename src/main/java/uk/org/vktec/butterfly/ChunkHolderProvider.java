// vim: noet

package uk.org.vktec.butterfly;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.util.math.ChunkPos;

public interface ChunkHolderProvider {
  public ChunkHolder getChunkHolderProxy(ChunkPos pos);
}
