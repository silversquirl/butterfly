// vim: noet

package uk.org.vktec.butterfly.mixin;

import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.org.vktec.butterfly.Butterfly;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity {
	private HopperBlockEntityMixin(BlockEntityType type) { super(type); }

	private static final ChunkTicketType LOAD_TICKET = ChunkTicketType.method_20628("butterfly:hopper", Long::compareTo, 1);

	@Inject(method = "getOutputInventory", at = @At("HEAD"))
	private void loadChunksOnGetOutputInventory(CallbackInfoReturnable cinfo) {
		if (this.world.isClient) {
			return;
		}

		Direction facing = (Direction)this.getCachedState().get(HopperBlock.FACING);
		BlockPos targetBlock = this.pos.offset(facing);
		ChunkPos targetChunk = new ChunkPos(targetBlock);

		// Optimization
		if (targetChunk.equals(new ChunkPos(this.pos))) return;

		Butterfly.loadImmediately((ServerWorld)world, targetChunk, Butterfly.LEVEL_TICKING);
	}
}
