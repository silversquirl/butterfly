// vim: noet

package uk.org.vktec.butterfly;

import net.minecraft.server.world.ThreadedAnvilChunkStorage;

public interface TickableChunkHolder {
  public void tickProxy(ThreadedAnvilChunkStorage tacs);
}
