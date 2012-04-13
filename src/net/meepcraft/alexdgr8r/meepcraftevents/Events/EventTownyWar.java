package net.meepcraft.alexdgr8r.meepcraftevents.Events;

import java.util.Random;

import net.meepcraft.alexdgr8r.meepcraftevents.EnumMeepEvent;
import net.meepcraft.alexdgr8r.meepcraftevents.MeepcraftEvents;

public class EventTownyWar extends MeepEvent {
	
	public boolean start(MeepcraftEvents plugin) {
		return plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "townyadmin toggle war");
	}
	
	public void end(MeepcraftEvents plugin) {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "townyadmin toggle war");
	}

	@Override
	public MeepEvent getNewInstance() {
		return new EventTownyWar();
	}

	@Override
	public EnumMeepEvent getEnum() {
		return EnumMeepEvent.TOWNY_WAR;
	}

	@Override
	public long getLengthOfEvent(Random random) {
		return 30 + random.nextInt(46);
	}

}
