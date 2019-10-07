// vim: noet

package uk.org.vktec.butterfly;

import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import java.util.List;

public class Butterfly {
	public static void bugle(World world, String message) {
		if (world.isClient) {
			// TODO
		} else {
			List players = ((ServerWorld)world).getServer().getPlayerManager().getPlayerList();
			players.forEach((player) -> {
				((ServerPlayerEntity)player).sendMessage(new LiteralText(message));
			});
		}
	}
}
