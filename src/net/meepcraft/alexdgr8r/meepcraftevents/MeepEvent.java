package net.meepcraft.alexdgr8r.meepcraftevents;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerLoginEvent;

public abstract class MeepEvent implements Comparable<MeepEvent> {
	
	/** How likely an event is to occur out of 100. Config value handled through main class. */
	public int rarity;

	/** Called at start of event. */
	public void start(MeepcraftEvents plugin) {}

	/** Called every 15 seconds during event. */
	public void update(MeepcraftEvents plugin) {}

	/** Called at end of event. */
	public void end(MeepcraftEvents plugin) {}
	
	/** Called when plugin starts to setup default values for event. */
	public void setDefaultValuesForEvent(MeepcraftEvents plugin) {}
	
	/** Load all configuration values. */
	public void loadConfigValues(FileConfiguration config, MeepcraftEvents plugin) {}
	
	/** Return an array of all the command names involved in this event */
	public String[] getCommandNames() {
		return null;
	}
	
	/** Handle any commands involed with this event */
	public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
	
	/** Called when MeepEvent is current event and when a player logs in. PRIORITY=NORMAL */
	public void playerLogin(PlayerLoginEvent event) {}

	@Override
	public final int compareTo(MeepEvent o) {
		return this.rarity - o.rarity;
	}
	
	/** Must give a new instance of event in order to reset values after event ends. */
	public abstract MeepEvent getNewInstance();
	
	/** Must give Enum for Event */
	public abstract EnumMeepEvent getEnum();
	
	/** Must give how long event lasts in Minutes */
	public abstract long getLengthOfEvent(Random random); 

}
