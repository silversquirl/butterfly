// vim: noet

package uk.org.vktec.butterfly.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.world.BlockView;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block {
	public FluidBlockMixin(Settings settings) {super(settings);}

	@Override
	public int getLightSubtracted(BlockState state, BlockView view, BlockPos pos) {
		return 3;
	}
}
