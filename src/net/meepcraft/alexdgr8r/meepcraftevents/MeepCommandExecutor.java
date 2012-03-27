package net.meepcraft.alexdgr8r.meepcraftevents;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeepCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		
		if (player == null) {
			sender.sendMessage("This command can only be performed by a player.");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("settrade")) {
			
		}
		
		return false;
	}

}
