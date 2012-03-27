package net.meepcraft.alexdgr8r.meepcraftevents;

public class EventMobAttack extends MeepEvent {

	@Override
	public MeepEvent getNewInstance() {
		return new EventMobAttack();
	}

	@Override
	public EnumMeepEvent getEnum() {
		return EnumMeepEvent.MOB_ATTACK;
	}

}
