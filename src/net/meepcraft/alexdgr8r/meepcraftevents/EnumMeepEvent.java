package net.meepcraft.alexdgr8r.meepcraftevents;

public enum EnumMeepEvent {
	
	NONE(0, "none", -1, null),
	TRADE_HOUR(1, "tradehour", 15, new EventTradeHour()),
	MOB_ATTACK(2, "mobattack", 5, new EventMobAttack());
	
	private int ID;
	private String ConfigName;
	private int DefaultRarity;
	private MeepEvent meepEvent;
	
	EnumMeepEvent(int id, String configName, int defRarity, MeepEvent event) {
		ID = id;
		ConfigName = configName;
		DefaultRarity = defRarity;
		meepEvent = event;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getConfigName() {
		return ConfigName;
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
		MeepcraftEvents.log.info("[MeepcraftEvents] Was not able to get event based off given ID.");
		return NONE;
	}

}
