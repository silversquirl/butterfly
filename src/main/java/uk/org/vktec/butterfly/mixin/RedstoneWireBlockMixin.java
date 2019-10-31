// vim: noet
package uk.org.vktec.butterfly.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.org.vktec.butterfly.Butterfly;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin extends Block {
	private RedstoneWireBlockMixin(Block.Settings settings) { super(settings); }

	@Inject(method = "update", at = @At("HEAD"))
	private void loadChunkOnUpdate(World world, BlockPos pos, BlockState state, CallbackInfoReturnable cinfo) {
		if (!world.isClient) {
			Butterfly.loadImmediately((ServerWorld)world, new ChunkPos(pos), Butterfly.LEVEL_TICKING);
		}
	}
}
