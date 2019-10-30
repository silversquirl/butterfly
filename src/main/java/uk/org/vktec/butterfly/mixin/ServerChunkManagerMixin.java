// vim: noet

package uk.org.vktec.butterfly.mixin;

import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import uk.org.vktec.butterfly.ChunkHolderProvider;
import uk.org.vktec.butterfly.TickableChunkHolder;
import uk.org.vktec.butterfly.LoadableServerChunkManager;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin extends ChunkManager implements LoadableServerChunkManager {
  @Shadow
  ThreadedAnvilChunkStorage threadedAnvilChunkStorage;

  public void forceLoadChunk(ChunkPos pos) {
		// Given the ChunkPos of a chunk WITH A SUBMITTED ChunkTicket, loads
		// the given chunk immediately rather than at the start/end of the
		// tick.

		// Get the associated ChunkHolder
		ChunkHolder holder = ((ChunkHolderProvider)threadedAnvilChunkStorage).getChunkHolderProxy(pos);
		if (holder == null) {
			// A ChunkTicket probably wasn't submitted
			return;
		}
		// Tick the ChunkHolder
		((TickableChunkHolder)holder).tickProxy(threadedAnvilChunkStorage);
  }
}
