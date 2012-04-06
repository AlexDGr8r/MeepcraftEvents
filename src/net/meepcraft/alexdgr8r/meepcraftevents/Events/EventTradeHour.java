package net.meepcraft.alexdgr8r.meepcraftevents.Events;

import java.util.Random;

import net.meepcraft.alexdgr8r.meepcraftevents.EnumMeepEvent;
import net.meepcraft.alexdgr8r.meepcraftevents.MeepcraftEvents;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventTradeHour extends MeepEvent {
	
	public Location MarketLocation;
	public String Message;
	public String endMessage;
	public boolean isTradeHour = false;
	
	private int updateTicks = 0;

	@Override
	public boolean start(MeepcraftEvents plugin) {
		isTradeHour = true;
		plugin.getServer().broadcast(Message, "meep.tradehour");
		return true;
	}

	@Override
	public void update(MeepcraftEvents plugin) {
		updateTicks++;
		if (updateTicks >= 12) {
			plugin.getServer().broadcast(Message, "meep.tradehour");
			updateTicks = 0;
		}
	}

	@Override
	public void end(MeepcraftEvents plugin) {
		isTradeHour = false;
		plugin.getServer().broadcast(endMessage, "meep.tradehour");
	}
	
	public void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("meep.tradehour")) {
			player.sendMessage(Message);
		}
	}
	
	@Override
	public void setDefaultValuesForEvent(MeepcraftEvents plugin) {
		for (World world : plugin.getServer().getWorlds()) {
			if (world != null) {
				if (world.getEnvironment() == Environment.NORMAL) {
					Location loc = world.getSpawnLocation();
					plugin.setDefault("tradehour.location.x", loc.getX());
					plugin.setDefault("tradehour.location.y", loc.getY());
					plugin.setDefault("tradehour.location.z", loc.getZ());
					plugin.setDefault("tradehour.location.world", loc.getWorld().getName());
					plugin.setDefault("tradehour.location.yaw", String.valueOf(loc.getYaw()));
					plugin.setDefault("tradehour.location.pitch", String.valueOf(loc.getPitch()));
					break;
				}
			}
		}
		plugin.setDefault("tradehour.message", ChatColor.AQUA.toString() + "It is now Trade Hour! Do /tradehour to make some great deals at the market!");
		plugin.setDefault("tradehour.EndingMessage", ChatColor.DARK_GREEN.toString() + "I'm sorry, but Trade Hour is now over. :( Please leave by either doing /back or /spawn");
	}
	
	@Override
	public void loadConfigValues(FileConfiguration config, MeepcraftEvents plugin) {
		double x = config.getDouble("tradehour.location.x");
		double y = config.getDouble("tradehour.location.y");
		double z = config.getDouble("tradehour.location.z");
		String worldName = config.getString("tradehour.location.world");
		float yaw = Float.parseFloat(config.getString("tradehour.location.yaw"));
		float pitch = Float.parseFloat(config.getString("tradehour.location.pitch"));
		MarketLocation = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
		Message = config.getString("tradehour.message");
		endMessage = config.getString("tradehour.EndingMessage");
	}

	@Override
	public MeepEvent getNewInstance() {
		return new EventTradeHour();
	}

	@Override
	public EnumMeepEvent getEnum() {
		return EnumMeepEvent.NONE;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"settrade", "tradehour"};
	}
	
	@Override
	public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		if (player == null) {
			sender.sendMessage("This command can only be performed by a player.");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("settrade")) {
			MarketLocation = player.getLocation();
			saveMarketLocation(MeepcraftEvents.config);
			player.sendMessage(ChatColor.DARK_GREEN + "Trade Hour Location set. You can test using /tradehour");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tradehour")) {
			if (isTradeHour || player.hasPermission("meep.settrade")) {
				if (player.teleport(MarketLocation)) {
					player.sendMessage(ChatColor.GREEN + "Welcome to the Trade Hour Market!");
				}
			} else {
				player.sendMessage(ChatColor.RED + "It is not currently Trade Hour.");
			}
			return true;
		}
		return false;
	}
	
	public void saveMarketLocation(FileConfiguration config) {
		try {
			config.set("tradehour.location.x", MarketLocation.getX());
			config.set("tradehour.location.y", MarketLocation.getY());
			config.set("tradehour.location.z", MarketLocation.getZ());
			config.set("tradehour.location.world", MarketLocation.getWorld().getName());
			config.set("tradehour.location.yaw", String.valueOf(MarketLocation.getYaw()));
			config.set("tradehour.location.pitch", String.valueOf(MarketLocation.getPitch()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getLengthOfEvent(Random random) {
		return 60;
	}

}
