package uk.org.vktec.butterfly.mixin;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.org.vktec.butterfly.PromotableChunkHolder;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {
	@Shadow protected abstract ChunkHolder getChunkHolder(long chunk);

	Set<Long> visitedChunks = new HashSet<Long>();

	private static boolean levelIsTicking(int level) {
		return ChunkHolder.getLevelType(level).isAfter(ChunkHolder.LevelType.TICKING);
	}

	@Inject(method = "setLevel", at = @At("RETURN"))
	private void updatePromotionsOnLevelSet(long chunk, int level, ChunkHolder passedInChunkHolder, int old_level, CallbackInfoReturnable<ChunkHolder> cinfo) {
		// If we're already in the process of updating promotions, we don't want to restart that
		if (!this.visitedChunks.isEmpty()) {
			return;
		}

		if (levelIsTicking(level)) {
			this.updatePromotions(cinfo.getReturnValue(), chunk);
		}
	}

	private void updatePromotions(ChunkHolder currentChunk, long chunk) {
		try {
			this.updatePromotions(currentChunk, ChunkPos.getPackedX(chunk), ChunkPos.getPackedZ(chunk));
		} finally {
			this.visitedChunks.clear();
		}
	}

	private void updatePromotions(ChunkHolder currentChunk, int x, int z) {
		// Don't revisit chunks
		long chunk = ChunkPos.toLong(x, z);
		if (this.visitedChunks.contains(chunk)) {
			return;
		}
		this.visitedChunks.add(chunk);

		ChunkHolder checkedChunk;
		boolean promoted = true;

		// Check the 5x5 area around this chunk
		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				// Skip the current chunk - we know that's ticking
				if (dx == 0 && dz == 0) continue;

				checkedChunk = this.getChunkHolder(ChunkPos.toLong(x + dx, z + dz));
				if (checkedChunk == null || !levelIsTicking(checkedChunk.getLevel())) {
					// If any of the chunks within the 5x5 is non-ticking, we don't promote
					promoted = false;
				} else {
					this.updatePromotions(checkedChunk, x + dx, z + dz);
				}
			}
		}

		((PromotableChunkHolder)currentChunk).setPromoted(promoted);
	}
}
