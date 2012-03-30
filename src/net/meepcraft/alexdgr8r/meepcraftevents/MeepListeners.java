package net.meepcraft.alexdgr8r.meepcraftevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MeepListeners implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
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
	
}
