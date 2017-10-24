package me.perryplaysmc.sumteams.Teams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import me.perryplaysmc.sumteams.SUM;
import me.perryplaysmc.sumteams.Utils.Config;

public class TeamCompleter implements TabCompleter {

	@SuppressWarnings("deprecation")
	@Override
	public List<String> onTabComplete(CommandSender s, Command ccc, String cl, String[] args) {
		List<String> t = Arrays.asList("create", "disband", "invite", "kick", "chat", "leave", "setowner", "accept", "status");
		List<String> pl = new ArrayList<String>();
		List<String> pls = new ArrayList<String>();
		Team te = SUM.getInstance().getManager().getTeamByPlayer((Player) s);
		if(te != null) {
			for(String  p : te.getPlayers()) {
				pl.add(p);
			}
		}
		if(te != null) {
			Config c = new Config(SUM.getInstance(), te.getName(), false);
			for(Player p : Bukkit.getOnlinePlayers()) {
				pls.add(p.getName());
				for(String  pe : c.getConfig().getStringList("Players")) {
					pls.remove(pe);
				}
			}
			pls.remove(te.getOwner());
		}
		List<String> f = Lists.newArrayList();

		if(args.length == 1) {
			for(String a : t) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase())) f.add(a);
			}
			return f;
		}
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("invite")) {
				for(String a : pls) {
					if(a.toLowerCase().startsWith(args[1].toLowerCase())) f.add(a);
				}
				return f;
			}
			else if(args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("setowner")) {
				for(String a : pl) {
					if(a.toLowerCase().startsWith(args[1].toLowerCase())) f.add(a);
				}
			return f;
			}
		}
		if(args.length >= 3) {
			if("".startsWith(args[0])) f.add("");
			return f;
		}

		return null;
	}

}
