package net.meepcraft.alexdgr8r.meepcraftevents.Events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import net.meepcraft.alexdgr8r.meepcraftevents.EnumMeepEvent;
import net.meepcraft.alexdgr8r.meepcraftevents.MeepcraftEvents;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class EventDoubleExp extends MeepEvent {
	
	public HashMap<Player, Integer> expGain = new HashMap<Player, Integer>();
	public HashMap<Player, Integer> levelGain = new HashMap<Player, Integer>();
	
	public boolean start(MeepcraftEvents plugin) {
		plugin.getServer().broadcastMessage(ChatColor.GOLD + "You will now receive Double Experience for the next hour!");
		plugin.getServer().broadcastMessage(ChatColor.GOLD + "Let's see who can get the most experience.");
		return true;
	}
	
	public void end(MeepcraftEvents plugin) {
		plugin.getServer().broadcastMessage(ChatColor.GOLD + "The Double Experience event is now over!");
		plugin.getServer().broadcastMessage(ChatColor.GOLD + "======Top Exp Earners======");
		List<Player> players = new ArrayList<Player>();
		for (Entry<Player, Integer> p : expGain.entrySet()) {
			players.add(p.getKey());
		}
		Collections.sort(players, new Comparator<Player>() {
			public int compare(Player o1, Player o2) {
				return expGain.get(o2) - expGain.get(o1);
			}
		});
		for (int i = 0; i < (players.size() < 5 ? players.size() : 5); i++) {
			plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + (i + 1) + ". " + players.get(i).getDisplayName() + ChatColor.GOLD + " - Exp. Gained: " + expGain.get(players.get(i))
					+ " - Lvls: " + levelGain.get(players.get(i)));
		}
		if (MeepcraftEvents.economy != null) {
			for (int i = 0; i < players.size(); i++) {
				int amount = 3 * expGain.get(players.get(i));
				MeepcraftEvents.economy.depositPlayer(players.get(i).getName(), amount);
				if (players.get(i).isOnline()) {
					players.get(i).sendMessage(ChatColor.GOLD + "You just received " + amount + " gold coins for this event!");
				}
			}
		}
	}
	
	public void playerExpChange(PlayerExpChangeEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			event.getPlayer().sendMessage(ChatColor.RED + "Double Experience doesn't work in creative!");
			return;
		}
		event.setAmount(event.getAmount() * 2);
		if (!expGain.containsKey(event.getPlayer())) {
			expGain.put(event.getPlayer(), event.getAmount());
		} else {
			expGain.put(event.getPlayer(), expGain.get(event.getPlayer()) + event.getAmount());
		}
	}
	
	public void playerLevelChange(PlayerLevelChangeEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (!levelGain.containsKey(event.getPlayer())) {
			levelGain.put(event.getPlayer(), 1);
		} else {
			levelGain.put(event.getPlayer(), levelGain.get(event.getPlayer()) + 1);
		}
	}
	
	public void playerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(ChatColor.GOLD + "It is currently Double Experience Hour!");
	}

	@Override
	public MeepEvent getNewInstance() {
		return new EventDoubleExp();
	}

	@Override
	public EnumMeepEvent getEnum() {
		return EnumMeepEvent.DOUBLE_EXP;
	}

	@Override
	public long getLengthOfEvent(Random random) {
		return 60;
	}

}
