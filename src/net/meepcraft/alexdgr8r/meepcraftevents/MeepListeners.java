package net.meepcraft.alexdgr8r.meepcraftevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class MeepListeners implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void login(PlayerLoginEvent event) {
		EnumMeepEvent mEvent = MeepcraftEvents.getCurrentEvent();
		if (mEvent != EnumMeepEvent.NONE) {
			mEvent.getMeepEvent().playerLogin(event);
		}
	}
	
}
