package net.meepcraft.alexdgr8r.meepcraftevents;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
		return 0;
	}
	
	public void spawnMobs(Player player, MeepcraftEvents plugin) {
		
	}

}
