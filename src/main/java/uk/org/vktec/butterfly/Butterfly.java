// vim: noet
package uk.org.vktec.butterfly;

import java.util.List;
import net.minecraft.world.World;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.ChunkPos;
import uk.org.vktec.butterfly.iface.ChunkManagerExtra;

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

	private static final ChunkTicketType BUTTERFLY_TICKET = ChunkTicketType.method_20628("butterfly", Long::compareTo, 16);
	public static final int LEVEL_TICKING = 1;
	public static final int LEVEL_ENTITY = 1;

	public static void loadImmediately(ServerWorld world, ChunkPos pos, int level) {
		ServerChunkManager manager = world.method_14178();
		manager.addTicket(BUTTERFLY_TICKET, pos, level, world.getTime());
		((ChunkManagerExtra)manager).levelUpdate(pos);
	}
}
