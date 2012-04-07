package net.meepcraft.alexdgr8r.meepcraftevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class MeepListeners implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		EnumMeepEvent mEvent = MeepcraftEvents.getCurrentEvent();
		if (mEvent != EnumMeepEvent.NONE) {
			mEvent.getMeepEvent().playerJoin(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		EnumMeepEvent mEvent = MeepcraftEvents.getCurrentEvent();
		if (mEvent != EnumMeepEvent.NONE) {
			mEvent.getMeepEvent().playerQuit(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		EnumMeepEvent mEvent = MeepcraftEvents.getCurrentEvent();
		if (mEvent != EnumMeepEvent.NONE) {
			mEvent.getMeepEvent().entityDeath(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onExpChange(PlayerExpChangeEvent event) {
		EnumMeepEvent mEvent = MeepcraftEvents.getCurrentEvent();
		if (mEvent != EnumMeepEvent.NONE) {
			mEvent.getMeepEvent().playerExpChange(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLevelChange(PlayerLevelChangeEvent event) {
		EnumMeepEvent mEvent = MeepcraftEvents.getCurrentEvent();
		if (mEvent != EnumMeepEvent.NONE) {
			mEvent.getMeepEvent().playerLevelChange(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onServerCommand(ServerCommandEvent event) {
		EnumMeepEvent mEvent = MeepcraftEvents.getCurrentEvent();
		if (mEvent != EnumMeepEvent.NONE) {
			mEvent.getMeepEvent().serverCommand(event);
		}
	}
	
}
