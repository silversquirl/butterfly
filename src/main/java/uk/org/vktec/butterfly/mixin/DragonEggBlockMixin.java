// vim: noet

package uk.org.vktec.butterfly.mixin;

import java.util.Random;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import uk.org.vktec.butterfly.Butterfly;

@Mixin(DragonEggBlock.class)
public abstract class DragonEggBlockMixin extends FallingBlock {
	private static boolean interceptUpdates = false;

	public DragonEggBlockMixin(Settings settings) {super(settings);}

	// TODO: replace this with a redirect
	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!world.isClient) {
			this.tryStartFallingReplacement(world, pos);
		}
	}

	private void tryStartFallingReplacement(World world, BlockPos pos) {
		if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= 0) {
			if (world.getChunkManager().shouldTickChunk(new ChunkPos(pos))) {
				// Default behaviour
				FallingBlockEntity falling = new FallingBlockEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, pos.getZ() + 0.5D, world.getBlockState(pos));
				this.configureFallingBlockEntity(falling);
				world.spawnEntity(falling);
			} else {
				// "Buggy" behaviour from 1.12
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
				BlockPos targetPos;

				for (targetPos = pos; FallingBlock.canFallThrough(world.getBlockState(targetPos)) && targetPos.getY() > 0; targetPos = targetPos.down()) {
				}

				if (targetPos.getY() > 0) {
					try {
						DragonEggBlockMixin.interceptUpdates = true; // setBlockState doesn't properly respect the flag, so we have to step in and fix it ourselves
						world.setBlockState(targetPos, this.getDefaultState(), 2);
					} finally { // Learn from Minecraft's mistakes - guard flags with try/finally :)
						DragonEggBlockMixin.interceptUpdates = false;
					}
				}
			}
		}
	}

	@Override
	public void updateNeighborStates(BlockState state, IWorld world, BlockPos pos, int i) {
		if (!DragonEggBlockMixin.interceptUpdates) {
			super.updateNeighborStates(state, world, pos, i);
		}
	}
}
