package net.meepcraft.alexdgr8r.meepcraftevents;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventMobAttack extends MeepEvent {
	
	public void start(MeepcraftEvents plugin) {
		plugin.getServer().broadcastMessage(ChatColor.RED + "Take up arms! The horde arrives!! Hide the women and children!");
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("meep.avoidmob")) {
				spawnMobs(player, plugin);
			}	
		}
	}
	
	public void update(MeepcraftEvents plugin) {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("meep.avoidmob")) {
				spawnMobs(player, plugin);
			}
		}
	}
	
	public void end(MeepcraftEvents plugin) {
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "The battle is over! Head back to your homes and rest. You deserve it.");
	}
	
	public void playerLogin(PlayerLoginEvent event) {
		event.getPlayer().sendMessage(ChatColor.RED + "Mobs are attacking! Prepare quickly for battle!");
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
		return random.nextInt(31) + 5;
	}
	
	public void spawnMobs(Player player, MeepcraftEvents plugin) {
		Location loc = player.getLocation();
		List<Entity> entitiesAround = player.getNearbyEntities(30, 30, 30);
		int monstersAround = 0;
		for (int i = 0; i < entitiesAround.size(); i++) {
			if (entitiesAround.get(i) instanceof Monster) {
				monstersAround++;
			}
		}
		int spawnTries = 0;
		while (monstersAround < 15) {
			Location randLoc = getRandomLocation(loc, plugin.rand);
			int tries = 0;
			while (loc.getWorld().spawnCreature(randLoc, getEntityTypeBasedOnEnv(loc.getWorld().getEnvironment(), plugin.rand)) == null) {
				randLoc = getRandomLocation(loc, plugin.rand);
				if (++tries > 10) {
					break;
				}
			}
			if (tries <= 10) {
				monstersAround++;
			}
			if (++spawnTries >= 5) {
				break;
			}
		}
	}
	
	public Location getRandomLocation(Location loc, Random random) {
		double x = (double)random.nextInt(15);
		double y = (double)random.nextInt(15);
		double z = (double)random.nextInt(15);
		return new Location(loc.getWorld(), x, y, z);
	}
	
	public EntityType getEntityTypeBasedOnEnv(Environment env, Random random) {
		if (env == Environment.NORMAL) {
			int r = random.nextInt(11);
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

}
