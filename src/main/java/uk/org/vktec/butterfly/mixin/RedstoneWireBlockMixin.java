// vim: noet
//
package uk.org.vktec.butterfly.mixin;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.Block;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.org.vktec.butterfly.LoadableServerChunkManager;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin extends Block {
	private RedstoneWireBlockMixin(Block.Settings settings) { super(settings); }
	private static final ChunkTicketType LOAD_TICKET = ChunkTicketType.method_20628("butterfly:redstone", Long::compareTo, 16);

	@Inject(method = "update", at = @At("HEAD"))
	private void loadChunkOnUpdate(World world, BlockPos pos, BlockState state, CallbackInfoReturnable cinfo) {
		// Attempts to load the current chunk when redstone dust is updated

		// Only try to load chunks on the logical server
		if (!world.isClient) {
			// Get the world's ChunkManager
			ServerChunkManager chunkManager = ((ServerWorld)world).method_14178();

			// Add a ticket to load the chunk for 16 ticks
			chunkManager.addTicket(LOAD_TICKET, new ChunkPos(pos), 1, world.getTime());

			// Force the chunks to be loaded immediately instead of at the
			// start/end of a tick
			((LoadableServerChunkManager)chunkManager).forceLoadChunk(new ChunkPos(pos));
		}
	}
}
