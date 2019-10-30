// vim: noet

package uk.org.vktec.butterfly.mixin;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uk.org.vktec.butterfly.PromotableChunkHolder;
import uk.org.vktec.butterfly.TickableChunkHolder;

@Mixin(ChunkHolder.class)
public abstract class ChunkHolderMixin implements PromotableChunkHolder, TickableChunkHolder {
	@Shadow
	int level;

	@Shadow
	protected abstract void tick(ThreadedAnvilChunkStorage class_3898_1);

	private boolean promoted;

	@Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ChunkHolder;level:I", opcode = Opcodes.GETFIELD))
	private int fakeLevelForTick(ChunkHolder self) {
		// tick() checks the level in order to initialize entityTickingFuture, which is what's actually checked when determining whether to tick entities
		// Since the entity ticking code doesn't care about the chunk's load level, only whether it has an entityTickingFuture, it's sufficient to fake it here and nowhere else
		// Unfortunately, since the `promoted` field doesn't get saved, this won't survive server restarts, but non-forceload chunk loading doesn't either so it's not a big deal

		if (this.promoted && ChunkHolder.getLevelType(this.level) == ChunkHolder.LevelType.TICKING) {
			// We've been promoted to an entity ticking chunk
			return 31;
		} else {
			return this.level;
		}
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	public void tickProxy(ThreadedAnvilChunkStorage tacs) {
		this.tick(tacs);
	}
}
