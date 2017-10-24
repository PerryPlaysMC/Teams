package me.perryplaysmc.sumteams.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.perryplaysmc.sumteams.SUM;

public abstract interface Utils {

	public default void sendMessage(CommandSender s, String... msgs) {
		SUM st = SUM.getInstance();
		for(String msg : msgs) {
			s.sendMessage((st.getConfig().getString("prefix") + " " + msg + "\n").trim().replace("&", "§"));
		}
	}
	
	public default void broadcast(String... msgs) {
		SUM st = SUM.getInstance();
		for(String msg : msgs) {
			Bukkit.broadcastMessage((st.getConfig().getString("prefix") + " " + msg + "\n").trim().replace("&", "§"));
		}
	}
	
}
