package net.meepcraft.alexdgr8r.meepcraftevents;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class MeepEvent implements Comparable<MeepEvent> {
	
	public int rarity;
	public long lengthOfEvent;

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
	
	public String[] getCommandNames() {
		return null;
	}
	
	public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}

	@Override
	public final int compareTo(MeepEvent o) {
		return this.rarity - o.rarity;
	}
	
	/** Must give a new instance of event in order to reset values after event ends. */
	public abstract MeepEvent getNewInstance();
	
	public abstract EnumMeepEvent getEnum();

}
