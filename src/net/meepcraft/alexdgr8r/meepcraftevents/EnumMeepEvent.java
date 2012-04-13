package net.meepcraft.alexdgr8r.meepcraftevents;

import net.meepcraft.alexdgr8r.meepcraftevents.Events.*;

public enum EnumMeepEvent {
	
	NONE(0, "none", -1, null),
	MOB_ATTACK(1, "mobattack", 40, new EventMobAttack()),
	DOUBLE_EXP(2, "doubleexp", 45, new EventDoubleExp()),
	TOWNY_WAR(3, "townywar", 35, new EventTownyWar());
	
//  Unused:
//	TRADE_HOUR(1, "tradehour", 20, new EventTradeHour()),
//	FORCED_PVP(3, "forcedpvp", 25, new EventForcedPvP());
	
	private int ID;
	private String Name;
	private int DefaultRarity;
	private MeepEvent meepEvent;
	
	EnumMeepEvent(int id, String name, int defRarity, MeepEvent event) {
		ID = id;
		Name = name;
		DefaultRarity = defRarity;
		meepEvent = event;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return Name;
	}
	
	public int getDefaultRarity() {
		return DefaultRarity;
	}
	
	public MeepEvent getMeepEvent() {
		return meepEvent;
	}
	
	public void setNewInstanceOfEvent() {
		meepEvent = meepEvent.getNewInstance();
	}
	
	public EnumMeepEvent getEventFromID(int i) {
		for (EnumMeepEvent event : EnumMeepEvent.values()) {
			if (event.getID() == i) {
				return event;
			}
		}
		MeepcraftEvents.serverLog("Was not able to get event based off given ID.");
		return NONE;
	}

}
