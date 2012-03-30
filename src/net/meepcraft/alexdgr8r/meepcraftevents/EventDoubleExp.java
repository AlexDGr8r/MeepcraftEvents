package net.meepcraft.alexdgr8r.meepcraftevents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class EventDoubleExp extends MeepEvent {
	
	public HashMap<Player, Integer> expGain = new HashMap<Player, Integer>();
	
	public void start(MeepcraftEvents plugin) {
		plugin.getServer().broadcastMessage(ChatColor.GOLD + "You will now receive Double Experience for the next hour! Enjoy!");
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
			plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + (i + 1) + ". " + players.get(i).getDisplayName() + ChatColor.GOLD + " - Exp. Gained: " + expGain.get(players.get(i)));
		}
	}
	
	public void playerExpChange(PlayerExpChangeEvent event) {
		event.setAmount(event.getAmount() * 2);
		if (!expGain.containsKey(event.getPlayer())) {
			expGain.put(event.getPlayer(), event.getAmount());
		} else {
			expGain.put(event.getPlayer(), expGain.get(event.getPlayer()) + event.getAmount());
		}
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
