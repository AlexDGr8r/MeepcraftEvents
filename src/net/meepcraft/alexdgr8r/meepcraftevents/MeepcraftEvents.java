package net.meepcraft.alexdgr8r.meepcraftevents;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MeepcraftEvents extends JavaPlugin {
	
	private static EnumMeepEvent currentEvent;
	private static EnumMeepEvent lastEvent;
	
	public Random rand = new Random();
	public HashMap<Integer, Integer> EventRarity = new HashMap<Integer, Integer>();
	
	public static Logger log;
	public static FileConfiguration config;
	public static File configFile = new File("plugins" + File.separator + "MeepcraftEvents" + File.separator + "config.yml");

	public void onEnable() {
		log = this.getLogger();
		configurationSetup();
		LoadConfigValues();
		currentEvent = EnumMeepEvent.NONE;
		lastEvent = EnumMeepEvent.NONE;
		scheduleRandomDelayForEventStart();
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (currentEvent != EnumMeepEvent.NONE) {
					updateCurrentEvent();
				}
			}
		}, 300L, 300L);
		log.info("MeepcraftEvents plugin enabled!");
	}
	
	private long getRandomDelay() {
		return (long)((rand.nextInt(30) + 31) * 60 * 20);
	}
	
	private void scheduleRandomDelayForEventStart() {
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				if (currentEvent != EnumMeepEvent.NONE) {
					endCurrentEvent();
				} else {
					startNewEvent();
				}
			}
		}, getRandomDelay());
	}

	public void onDisable() {
		log.info("MeepcraftEvents plugin disabled!");
	}
	
	private void configurationSetup() {
		try {
			config = this.getConfig();
			configFile.mkdir();
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			for (EnumMeepEvent eEvent : EnumMeepEvent.values()) {
				if (eEvent != EnumMeepEvent.NONE) {
					eEvent.getMeepEvent().setDefaultValuesForEvent(this);
					setDefault(eEvent.getConfigName() + ".rarity", eEvent.getDefaultRarity());
				}
			}
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void setDefault(String path, Object value) {
		if (!config.contains(path)) {
			config.set(path, value);
		}
	}
	
	private void LoadConfigValues() {
		try {
			config.load(configFile);
			for (EnumMeepEvent eEvent : EnumMeepEvent.values()) {
				if (eEvent != EnumMeepEvent.NONE) {
					eEvent.getMeepEvent().loadConfigValues(config, this);
					eEvent.getMeepEvent().rarity = config.getInt(eEvent.getConfigName() + ".rarity");
					EventRarity.put(eEvent.getID(), eEvent.getMeepEvent().rarity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startNewEvent() {
		if (currentEvent == EnumMeepEvent.NONE) {
			List<MeepEvent> possibleEvents;
			if (lastEvent == EnumMeepEvent.NONE) {
				possibleEvents = getAllEvents();
			} else {
				possibleEvents = getAllEvents(lastEvent);
			}
			Collections.sort(possibleEvents);
			int r = rand.nextInt(101);
			for (int i = 0; i < possibleEvents.size(); i++) {
				MeepEvent event = possibleEvents.get(i);
				if (r <= event.rarity) {
					currentEvent = event.getEnum();
					currentEvent.getMeepEvent().start(this);
					this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						public void run() {
							endCurrentEvent();
						}
					}, currentEvent.getMeepEvent().lengthOfEvent);
					break;
				}
			}
		}
	}
	
	public void updateCurrentEvent() {
		MeepEvent event = currentEvent.getMeepEvent();
		if (event != null) {
			event.update(this);
		}
	}
	
	public void endCurrentEvent() {
		MeepEvent event = currentEvent.getMeepEvent();
		if (event == null) {
			scheduleRandomDelayForEventStart();
			return;
		}
		event.end(this);
		currentEvent.setNewInstanceOfEvent();
		currentEvent.getMeepEvent().loadConfigValues(config, this);
		currentEvent.getMeepEvent().rarity = EventRarity.get(currentEvent.getID());
		lastEvent = currentEvent;
		currentEvent = EnumMeepEvent.NONE;
		scheduleRandomDelayForEventStart();
	}
	
	private List<MeepEvent> getAllEvents() {
		List<MeepEvent> events = new ArrayList<MeepEvent>();
		for (EnumMeepEvent e : EnumMeepEvent.values()) {
			if (e != EnumMeepEvent.NONE) {
				events.add(e.getMeepEvent());
			}
		}
		return events;
	}
	
	private List<MeepEvent> getAllEvents(EnumMeepEvent discludedEvent) {
		List<MeepEvent> events = new ArrayList<MeepEvent>();
		for (EnumMeepEvent e : EnumMeepEvent.values()) {
			if (e != EnumMeepEvent.NONE && e != discludedEvent) {
				events.add(e.getMeepEvent());
			}
		}
		return events;
	}
	
	public static EnumMeepEvent getCurrentEvent() {
		return currentEvent;
	}
	
	public static EnumMeepEvent getLastEvent() {
		return lastEvent;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		for (MeepEvent event : getAllEvents()) {
			if (event != null) {
				String[] cmds = event.getCommandNames();
				if (cmds != null) {
					for (int i = 0; i < cmds.length; i++) {
						if (cmd.getName().equalsIgnoreCase(cmds[i])) {
							return event.handleCommand(sender, cmd, label, args);
						}
					}
				}
			}
		}
		return false;
	}
	
}
