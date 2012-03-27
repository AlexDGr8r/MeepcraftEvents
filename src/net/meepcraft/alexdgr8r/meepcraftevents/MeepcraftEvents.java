package net.meepcraft.alexdgr8r.meepcraftevents;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MeepcraftEvents extends JavaPlugin {
	
	private static EnumMeepEvent currentEvent;
	private static EnumMeepEvent lastEvent;
	private int waitingTaskID = -1;
	private int runningTaskID = -1;
	
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
		return (long)((rand.nextInt(31) + 30) * 60 * 20);
	}
	
	private void scheduleRandomDelayForEventStart() {
		waitingTaskID = this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
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
		this.getServer().getScheduler().cancelTasks(this);
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
					setDefault(eEvent.getName() + ".rarity", eEvent.getDefaultRarity());
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
					eEvent.getMeepEvent().rarity = config.getInt(eEvent.getName() + ".rarity");
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
					startNewEvent(event.getEnum());
					break;
				}
			}
		}
	}
	
	public void startNewEvent(EnumMeepEvent enumEvent) {
		currentEvent = enumEvent;
		currentEvent.getMeepEvent().start(this);
		runningTaskID = this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				endCurrentEvent();
			}
		}, currentEvent.getMeepEvent().getLengthOfEvent(rand)*60*20);
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
		if (cmd.getName().equalsIgnoreCase("startevent")) {
			if (currentEvent != EnumMeepEvent.NONE) {
				sender.sendMessage(ChatColor.RED + "Error: There is another event already in progress.");
				return true;
			} else if (args.length >= 1) {
				for (MeepEvent event : getAllEvents()) {
					if (event.getEnum() == EnumMeepEvent.NONE) {
						continue;
					}
					if (args[0].equalsIgnoreCase(event.getEnum().getName())) {
						this.getServer().getScheduler().cancelTask(waitingTaskID);
						startNewEvent(event.getEnum());
						sender.sendMessage("Event started!");
						return true;
					}
				}
				sender.sendMessage(ChatColor.RED + "Error: Event not found!");
				return true;
			}
			return false;
		} else if (cmd.getName().equalsIgnoreCase("endevent")) {
			if (currentEvent == EnumMeepEvent.NONE) {
				sender.sendMessage("There is no event in progress!");
				return true;
			} else {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase(currentEvent.getName())) {
						this.getServer().getScheduler().cancelTask(runningTaskID);
						endCurrentEvent();
						sender.sendMessage("Event ended.");
						return true;
					} else {
						sender.sendMessage("There is no event by that name in progress.");
						return true;
					}
				}
				return false;
			}
		}
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
