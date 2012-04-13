package net.meepcraft.alexdgr8r.meepcraftevents.Events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.meepcraft.alexdgr8r.meepcraftevents.EnumMeepEvent;
import net.meepcraft.alexdgr8r.meepcraftevents.MeepcraftEvents;

public class EventForcedPvP extends MeepEvent {
	
	public HashMap<Player, Integer> playerKills = new HashMap<Player, Integer>();
	
	public boolean start(MeepcraftEvents plugin) {
//		if (MeepcraftEvents.pvp == null) return false;
		
		plugin.getServer().broadcastMessage(ChatColor.RED + "It is now time for Forced PvP throughout all worlds!");
		plugin.getServer().broadcastMessage(ChatColor.RED + "War enabled in Towny world!");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "townyadmin toggle war");
		setAllPvP(plugin, true);
		
		return true;
	}
	
	public void update(MeepcraftEvents plugin) {
		setAllPvP(plugin, true);
	}
	
	public void end(MeepcraftEvents plugin) {
//		if (MeepcraftEvents.pvp == null) return;
		
		setAllPvP(plugin, false);
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "townyadmin toggle war");
		plugin.getServer().broadcastMessage(ChatColor.GREEN + "Forced PvP is now over!");
		plugin.getServer().broadcastMessage(ChatColor.GREEN + "======Top Player Killers======");
		List<Player> players = new ArrayList<Player>();
		for (Entry<Player, Integer> p : playerKills.entrySet()) {
			players.add(p.getKey());
		}
		Collections.sort(players, new Comparator<Player>() {
			public int compare(Player o1, Player o2) {
				return playerKills.get(o2) - playerKills.get(o1);
			}
		});
		for (int i = 0; i < (players.size() < 5 ? players.size() : 5); i++) {
			plugin.getServer().broadcastMessage(ChatColor.GREEN + "" + (i + 1) + ". " + players.get(i).getDisplayName() + ChatColor.GREEN + " - Kills: " + playerKills.get(players.get(i)));
		}
		if (MeepcraftEvents.economy != null) {
			for (int i = 0; i < players.size(); i++) {
				int amount = 5 * playerKills.get(players.get(i));
				MeepcraftEvents.economy.depositPlayer(players.get(i).getName(), amount);
				if (players.get(i).isOnline()) {
					players.get(i).sendMessage(ChatColor.GOLD + "You just received " + amount + " gold coins for this event!");
				}
			}
		}
	}
	
	public void playerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(ChatColor.RED + "It is currently the Forced PvP event!");
		event.getPlayer().sendMessage(ChatColor.RED + "Try to kill as many players as possible!");
	}
	
	public void serverCommand(ServerCommandEvent event) {
		if (event.getCommand().toLowerCase().startsWith("pvp")) {
			event.setCommand("pvp on");
		}
	}

	@Override
	public MeepEvent getNewInstance() {
		return new EventForcedPvP();
	}

	@Override
	public EnumMeepEvent getEnum() {
		return EnumMeepEvent.NONE;
	}

	@Override
	public long getLengthOfEvent(Random random) {
		return random.nextInt(21) + 10;
	}
	
	public void setAllPvP(MeepcraftEvents plugin, boolean flag) {
//		for (Player player : plugin.getServer().getOnlinePlayers()) {
//			if (MeepcraftEvents.pvp.checkPlayerStatus(player, player.getWorld().getName()) != flag) {
//				MeepcraftEvents.pvp.setPlayerStatus(player, player.getWorld().getName(), flag);
//			}
//		}
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
				if (victim instanceof Player) {
					if (!playerKills.containsKey(player)) {
						playerKills.put(player, 1);
					} else {
						playerKills.put(player, playerKills.get(player) + 1);
					}
				}
			}
		}
	}

}
