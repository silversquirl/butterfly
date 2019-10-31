// vim: noet
package uk.org.vktec.butterfly.iface;

import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.util.math.ChunkPos;

public interface ChunkManagerExtra {
	public void levelUpdate(ChunkPos pos);
}
