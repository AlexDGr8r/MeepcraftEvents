package net.meepcraft.alexdgr8r.meepcraftevents;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class EventTradeHour extends MeepEvent {
	
	public Location MarketLocation;
	public String Message;
	public boolean isTradeHour = false;
	
	private int updateTicks = 0;

	@Override
	public void start(MeepcraftEvents plugin) {
		isTradeHour = true;
		plugin.getServer().broadcast(Message, "meep.tradehour");
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
		plugin.getServer().broadcast(ChatColor.DARK_GREEN + "I'm sorry, but Trade Hour is now over. :( Please leave by either doing /back or /spawn", "meep.tradehour");
	}
	
	@Override
	public void setDefaultValuesForEvent(MeepcraftEvents plugin) {
		for (World world : plugin.getServer().getWorlds()) {
			if (world != null) {
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
		plugin.setDefault("tradehour.message", ChatColor.AQUA.toString() + "It is now Trade Hour! Do /tradehour to make some great deals at the market!");
	}
	
	@Override
	public void loadConfigValues(FileConfiguration config, MeepcraftEvents plugin) {
		double x, y, z;
		String worldName;
		float yaw, pitch;
		x = config.getDouble("tradehour.location.x");
		y = config.getDouble("tradehour.location.y");
		z = config.getDouble("tradehour.location.z");
		worldName = config.getString("tradehour.location.world");
		yaw = Float.parseFloat(config.getString("tradehour.location.yaw"));
		pitch = Float.parseFloat(config.getString("tradehour.location.pitch"));
		MarketLocation = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
		Message = config.getString("tradehour.message");
	}

	@Override
	public MeepEvent getNewInstance() {
		return new EventTradeHour();
	}

	@Override
	public EnumMeepEvent getEnum() {
		return EnumMeepEvent.TRADE_HOUR;
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
			config.save(MeepcraftEvents.configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
