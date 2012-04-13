package net.meepcraft.alexdgr8r.meepcraftevents.Events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventMobAttack extends MeepEvent {
	
	public int ticksTillStart = 2;
	public int ticksForIntensity = 0;
	public int totalMobKills = 0;
	public boolean doubleExp = false;
	public boolean isAttacking = false;
	public Location westGates;
	public HashMap<Player, Integer> mobKills = new HashMap<Player, Integer>();
	public List<String> cheesyLines = new ArrayList<String>();
	
	public boolean start(MeepcraftEvents plugin) {
		isAttacking = true;
		createCheesyLines();
		plugin.getServer().broadcastMessage(ChatColor.RED + "Take up arms! Her army is arriving!!!");
		plugin.getServer().broadcastMessage(ChatColor.RED + "They will try and take over the spawn castle by breaching through the west gates!");
		plugin.getServer().broadcastMessage(ChatColor.RED + "Kill as many as you can!");
		plugin.getServer().broadcastMessage(ChatColor.RED + "If you wish to defend the west gates, use /fight");
		if (plugin.rand.nextInt(4) == 0) {
			doubleExp = true;
			plugin.getServer().broadcastMessage(ChatColor.GOLD + "You will also receive double experience during this event.");
		}
		World world = plugin.getServer().getWorld("spawnhub");
		if (world != null) {
			world.setTime(18000);
			world.setStorm(true);
		}
		return true;
	}
	
	public void update(MeepcraftEvents plugin) {
		if (--ticksTillStart > 0) {
			return;
		} else if (++ticksForIntensity % 4 == 0) {
			plugin.getServer().broadcastMessage(ChatColor.RED + "The West Gates are still under attack!");
			plugin.getServer().broadcastMessage(ChatColor.RED + "Come join the battle by using /fight");
		}
		if (ticksTillStart == 0) {
			plugin.getServer().broadcastMessage(ChatColor.RED + "HERE THEY COME!!!");
		}
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("meep.avoidmob")) {
				spawnMobs(player, plugin);
			}
		}
		World world = plugin.getServer().getWorld("spawnhub");
		if (world != null) {
			if (world.getTime() <= 16000 || world.getTime() >= 22000) {
				world.setTime(18000);
			}
		}
		if (plugin.rand.nextInt(4) == 0) {
			plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "[TYRANT] She" + ChatColor.WHITE + ": " + cheesyLines.get(plugin.rand.nextInt(cheesyLines.size())));
		}
	}
	
	public void createCheesyLines() {
		cheesyLines.add("Having fun? Muhahaha!!!!");
		cheesyLines.add("My army is hungry for your blood...");
		cheesyLines.add("Don't worry, I have more soldiers where those came from.");
		cheesyLines.add("Fight my minions!!!!");
		cheesyLines.add("Not even the doctor could save you now...");
		cheesyLines.add("Advance!!!");
		cheesyLines.add("Breach through the West Gates!!!!");
		cheesyLines.add("Blades getting dull yet? Muhaha!");
		cheesyLines.add("I'm not impressed...FIGHT HARDER MY ARMY!!!");
		cheesyLines.add("No, this isn't Meepcraft. This is SheCraft!!! >)");
		cheesyLines.add("Mmmm, I taste blood!");
		cheesyLines.add("Wow, Meepcraft players really suck!");
		cheesyLines.add("Not much further my minions!!!");
	}
	
	public void end(MeepcraftEvents plugin) {
		isAttacking = false;
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "The battle is over! She is retreating!");
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "I'm sure she'll be back. For now, rest and rebuild.");
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "======Best Soldiers======");
		List<Player> players = new ArrayList<Player>();
		for (Entry<Player, Integer> p : mobKills.entrySet()) {
			players.add(p.getKey());
		}
		Collections.sort(players, new Comparator<Player>() {
			public int compare(Player o1, Player o2) {
				return mobKills.get(o2) - mobKills.get(o1);
			}
		});
		for (int i = 0; i < (players.size() < 5 ? players.size() : 5); i++) {
			plugin.getServer().broadcastMessage(ChatColor.AQUA + "" + (i + 1) + ". " + players.get(i).getDisplayName() + ChatColor.AQUA + " - Kills: " + mobKills.get(players.get(i)));
		}
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "Overall we slaughtered " + totalMobKills + " of them! Good job!");
		if (MeepcraftEvents.economy != null) {
			for (int i = 0; i < players.size(); i++) {
				int amount = 8 * mobKills.get(players.get(i));
				MeepcraftEvents.economy.depositPlayer(players.get(i).getName(), amount);
				if (players.get(i).isOnline()) {
					players.get(i).sendMessage(ChatColor.GOLD + "You just received " + amount + " gold coins for this event!");
				}
			}
		}
		World world = plugin.getServer().getWorld("spawnhub");
		if (world != null) {
			world.setStorm(false);
			world.setTime(0);
		}
	}
	
	public void playerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(ChatColor.RED + "SHE's army is attacking!");
		event.getPlayer().sendMessage(ChatColor.RED + "If you want to join the battle, use /fight");
		if (doubleExp) {
			event.getPlayer().sendMessage(ChatColor.GOLD + "You will also receive double experience during battle.");
		}
	}
	
	public void playerExpChange(PlayerExpChangeEvent event) {
		if (doubleExp && event.getPlayer().getWorld().getName().equalsIgnoreCase("spawnhub")) {
			event.setAmount(event.getAmount() * 2);
		}
	}

	@Override
	public MeepEvent getNewInstance() {
		return new EventMobAttack();
	}

	@Override
	public EnumMeepEvent getEnum() {
		return EnumMeepEvent.MOB_ATTACK;
	}

	@Override
	public long getLengthOfEvent(Random random) {
		return random.nextInt(16) + 5;
	}
	
	public void spawnMobs(Player player, MeepcraftEvents plugin) {
		Location loc = player.getLocation();
		if (loc.getWorld() != plugin.getServer().getWorld("spawnhub")) return;
		List<Entity> entitiesAround = player.getNearbyEntities(30, 30, 30);
		int monstersAround = 0;
		for (int i = 0; i < entitiesAround.size(); i++) {
			if (entitiesAround.get(i) instanceof Monster) {
				monstersAround++;
			}
		}
		int spawnTries = 0;
		int spawnIntensity = (ticksForIntensity / 4) * 5;
		while (monstersAround < (15 + spawnIntensity)) {
			Location randLoc = getRandomLocation(loc, plugin.rand);
			int tries = 0;
			while (loc.getWorld().spawnCreature(randLoc, getEntityTypeBasedOnEnv(loc.getWorld().getEnvironment(), plugin.rand)) == null) {
				randLoc = getRandomLocation(loc, plugin.rand);
				if (++tries > 10) {
					break;
				}
			}
			if (tries <= 10) {
//				MeepcraftEvents.serverLog("Mob spawned around " + player.getName() + " " + randLoc.distance(loc) + " blocks away.");
				monstersAround++;
			}
			if (++spawnTries >= 15) {
				break;
			}
		}
	}
	
	public Location getRandomLocation(Location loc, Random random) {
		double x = (double)random.nextInt(15);
		double y = (double)random.nextInt(8);
		double z = (double)random.nextInt(15);
		if (random.nextBoolean()) x = -x;
		if (random.nextBoolean()) y = -y;
		if (random.nextBoolean()) z = -z;
		return new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
	}
	
	public EntityType getEntityTypeBasedOnEnv(Environment env, Random random) {
		if (env == Environment.NORMAL) {
			int r = random.nextInt(12);
			switch (r) {
			case 0:
			case 1:
			case 2:
				return EntityType.ZOMBIE;
			case 3:
			case 4:
			case 5:
				return EntityType.SKELETON;
			case 6:
			case 7:
				return EntityType.SPIDER;
			case 8:
			case 9:
				return EntityType.CAVE_SPIDER;
			case 10:
				return EntityType.CREEPER;
			case 11:
				return EntityType.ENDERMAN;
			}
		} else if (env == Environment.NETHER) {
			int r = random.nextInt(10);
			switch (r) {
			case 0:
			case 1:
			case 2:
			case 3:
				return EntityType.PIG_ZOMBIE;
			case 4:
			case 5:
			case 6:
				return EntityType.MAGMA_CUBE;
			case 7:
			case 8:
				return EntityType.BLAZE;
			case 9: 
				return EntityType.GHAST;
			}
		} else if (env == Environment.THE_END) {
			return EntityType.ENDERMAN;
		}
		return EntityType.ZOMBIE;
	}
	
	public void entityDeath(EntityDeathEvent event, MeepcraftEvents plugin) {
		if(!(event.getEntity() instanceof LivingEntity)) return;
		
		LivingEntity victim = (LivingEntity)event.getEntity();
		
		if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)victim.getLastDamageCause();
			Player player = null;
			if (e.getDamager() instanceof Player) {
				player = (Player)e.getDamager();
			} else if (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player) {
				player = (Player)((Projectile)e.getDamager()).getShooter();
			} else if (e.getDamager() instanceof Tameable) {
				Tameable tame = (Tameable)e.getDamager();
				if (tame.isTamed() && tame.getOwner() instanceof Player) {
					player = (Player)tame.getOwner();
				}
			}
			if (player != null) {
				if (player.getWorld() == plugin.getServer().getWorld("spawnhub")) {
					if (!(victim instanceof Player)) {
						if (!mobKills.containsKey(player)) {
							mobKills.put(player, 1);
						} else {
							mobKills.put(player, mobKills.get(player) + 1);
						}
						totalMobKills++;
					}
				}
			}
		}
	}
	
	public String[] getCommandNames() {
		return new String[] {"fight", "setwestgates"};
	}
	
	public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) player = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("fight") && player != null) {
			if (isAttacking) {
				player.teleport(westGates);
				player.sendMessage(ChatColor.RED + "Fight as hard as you can!");
			} else if (player.hasPermission("meep.setgates")) {
				player.teleport(westGates);
			} else {
				player.sendMessage(ChatColor.GREEN + "There is no sight of She's army currently.");
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("setwestgates") && player != null) {
			westGates = player.getLocation();
			saveWestGatesLocation(MeepcraftEvents.config);
			player.sendMessage(ChatColor.DARK_GREEN + "West Gates Location set. You can test using /fight");
			return true;
		}
		return false;
	}
	
	public void setDefaultValuesForEvent(MeepcraftEvents plugin) {
		for (World world : plugin.getServer().getWorlds()) {
			if (world != null) {
				if (world.getName().equalsIgnoreCase("spawnhub")) {
					Location loc = world.getSpawnLocation();
					plugin.setDefault("mobattack.westgates.x", loc.getX());
					plugin.setDefault("mobattack.westgates.y", loc.getY());
					plugin.setDefault("mobattack.westgates.z", loc.getZ());
					plugin.setDefault("mobattack.westgates.world", loc.getWorld().getName());
					plugin.setDefault("mobattack.westgates.yaw", String.valueOf(loc.getYaw()));
					plugin.setDefault("mobattack.westgates.pitch", String.valueOf(loc.getPitch()));
					break;
				}
			}
		}
	}
	
	public void loadConfigValues(FileConfiguration config, MeepcraftEvents plugin) {
		double x = config.getDouble("mobattack.westgates.x");
		double y = config.getDouble("mobattack.westgates.y");
		double z = config.getDouble("mobattack.westgates.z");
		String worldName = config.getString("mobattack.westgates.world");
		float yaw = Float.parseFloat(config.getString("mobattack.westgates.yaw"));
		float pitch = Float.parseFloat(config.getString("mobattack.westgates.pitch"));
		westGates = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
	}
	
	public void saveWestGatesLocation(FileConfiguration config) {
		try {
			config.set("tradehour.location.x", westGates.getX());
			config.set("tradehour.location.y", westGates.getY());
			config.set("tradehour.location.z", westGates.getZ());
			config.set("tradehour.location.world", westGates.getWorld().getName());
			config.set("tradehour.location.yaw", String.valueOf(westGates.getYaw()));
			config.set("tradehour.location.pitch", String.valueOf(westGates.getPitch()));
			MeepcraftEvents.queueSaveConfig = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
