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
	public boolean doubleExp = false;
	public boolean isAttacking = false;
	public HashMap<Player, Integer> mobKills = new HashMap<Player, Integer>();
	public List<Player> prayed = new ArrayList<Player>();
	
	public boolean start(MeepcraftEvents plugin) {
		isAttacking = true;
		plugin.getServer().broadcastMessage(ChatColor.RED + "Take up arms! Her army is arriving!!!");
		plugin.getServer().broadcastMessage(ChatColor.RED + "They will try and kill each one of you, one by one.");
		plugin.getServer().broadcastMessage(ChatColor.RED + "Kill as many as you can!");
		plugin.getServer().broadcastMessage(ChatColor.RED + "For you scared ones that don't want to fight, you better /pray!");
		if (plugin.rand.nextInt(4) == 0) {
			doubleExp = true;
			plugin.getServer().broadcastMessage(ChatColor.GOLD + "You will also receive double experience during this event.");
		}
		for (World world : plugin.getServer().getWorlds()) {
			world.setTime(18000);
			if (world.getEnvironment() == Environment.NORMAL) {
				world.setStorm(true);
			}
		}
		return true;
	}
	
	public void update(MeepcraftEvents plugin) {
		if (--ticksTillStart > 0) {
			return;
		}
		if (ticksTillStart == 0) {
			plugin.getServer().broadcastMessage(ChatColor.RED + "HERE THEY COME!!!");
		}
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("meep.avoidmob")) {
				if (!prayed.contains(player)) {
					spawnMobs(player, plugin);
				}
			}
		}
		for (World world : plugin.getServer().getWorlds()) {
			if (world.getTime() <= 16000 || world.getTime() >= 22000) {
				world.setTime(18000);
			}
		}
		if (plugin.rand.nextInt(4) == 0) {
			plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "[TYRANT] She" + ChatColor.WHITE + ": " + randomMessage(plugin.rand));
		}
	}
	
	public String randomMessage(Random random) {
		int i = random.nextInt(10);
		switch (i) {
		case 0:
			return "Having fun? Muhahaha!!!!";
		case 1:
			return "My army is hungry for your blood...";
		case 2:
			return "Don't worry, I have more soldiers where those came from.";
		case 3:
			return "Fight my minions!!!!";
		case 4:
			return "Not even the doctor could save you now...";
		case 5:
			return "Advance!!!";
		case 6:
			return "Breach through the West Gates!!!!";
		case 7:
			return "Blades getting dull yet? Muhaha!";
		case 8:
			return "I'm not impressed...FIGHT HARDER MY ARMY!!!";
		case 9:
			return "No, this isn't Meepcraft. This is SheCraft!!! >)";
		}
		return "Having fun? Muhahaha!!!!";
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
		if (MeepcraftEvents.economy != null) {
			for (int i = 0; i < players.size(); i++) {
				int amount = 8 * mobKills.get(players.get(i));
				MeepcraftEvents.economy.depositPlayer(players.get(i).getName(), amount);
				if (players.get(i).isOnline()) {
					players.get(i).sendMessage(ChatColor.GOLD + "You just received " + amount + " gold coins for this event!");
				}
			}
		}
		for (World world : plugin.getServer().getWorlds()) {
			world.setTime(0);
			if (world.getEnvironment() == Environment.NORMAL) {
				world.setStorm(false);
			}
		}
	}
	
	public void playerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(ChatColor.RED + "SHE's army is attacking! Prepare quickly for battle!");
		event.getPlayer().sendMessage(ChatColor.RED + "If you don't want to fight, go /pray");
		if (doubleExp) {
			event.getPlayer().sendMessage(ChatColor.GOLD + "You will also receive double experience during battle.");
		}
	}
	
	public void playerExpChange(PlayerExpChangeEvent event) {
		if (doubleExp) {
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
		return random.nextInt(26) + 5;
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
				MeepcraftEvents.serverLog("Mob spawned around " + player.getName() + " " + randLoc.distance(loc) + " blocks away.");
				monstersAround++;
			}
			if (++spawnTries >= 5) {
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
	
	public void entityDeath(EntityDeathEvent event) {
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
				if (!(victim instanceof Player)) {
					if (!mobKills.containsKey(player)) {
						mobKills.put(player, 1);
					} else {
						mobKills.put(player, mobKills.get(player) + 1);
					}
				}
			}
		}
	}
	
	public String[] getCommandNames() {
		return new String[] {"pray"};
	}
	
	public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) player = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("pray") && player != null) {
			if (!isAttacking) {
				player.sendMessage(ChatColor.GREEN + "There is no sight of She's army currently.");
				return true;
			}
			if (prayed.contains(player)) {
				player.sendMessage(ChatColor.GREEN + "You have already prayed to Notch for protection from She's army.");
			} else {
				prayed.add(player);
				player.sendMessage(ChatColor.GREEN + "The power of Notch will protect you from the onslaught of She's army.");
			}
			return true;
		}
		return false;
	}

}
