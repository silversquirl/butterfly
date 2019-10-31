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
import uk.org.vktec.butterfly.iface.ChunkManagerExtra;
import uk.org.vktec.butterfly.iface.ChunkHolderExtra;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin extends ChunkManager implements ChunkManagerExtra {
	@Shadow ThreadedAnvilChunkStorage threadedAnvilChunkStorage;
	@Shadow ChunkTicketManager ticketManager;

	@Shadow protected abstract ChunkHolder getChunkHolder(long pos);

	// Process updates to a chunk's load level
	public void levelUpdate(ChunkPos pos) {
		ChunkHolder holder = this.getChunkHolder(pos.toLong());
		if (holder == null) return; // The chunk isn't loaded and doesn't need to be. Thus, there is nothing to do
		((ChunkHolderExtra)holder).tickProxy(threadedAnvilChunkStorage);
	}
}
