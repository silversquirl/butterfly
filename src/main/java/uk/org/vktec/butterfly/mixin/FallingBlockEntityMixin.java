// vim: noet

package uk.org.vktec.butterfly.mixin;

import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
	private FallingBlockEntityMixin(EntityType type, World world) { super(type, world); }

	@Shadow
	public int timeFalling;

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/FallingBlockEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
	private void allowFloating(CallbackInfo cinfo) {
		this.timeFalling = 1;
	}

	@Inject(method = "tick", at = @At(value = "NEW", target = "net/minecraft/util/math/BlockPos", ordinal = 1))
	private void levitateOnFences(CallbackInfo cinfo) {
		if (FallingBlock.canFallThrough(this.world.getBlockState(new BlockPos(this.x, this.y - 0.01, this.z)))) {
			this.onGround = false;
		}

		// This is a check in entity.baseTick - Mojang are big brain and don't call baseTick from FallingBlockEntity.tick
		if (this.y < -64) {
			this.destroy();
		}
	}
}
