package me.perryplaysmc.sumteams.Teams;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import me.perryplaysmc.sumteams.SUM;
import me.perryplaysmc.sumteams.Utils.Config;

public class TeamManager {

	private Set<Team> teams;

	public TeamManager() {
		teams = new HashSet<>();
	}

	public void addTeam(Team t) {
		teams.add(t);
	}

	public void removeTeam(Team t) {
		teams.remove(t);
	}

	public Team getTeamByPlayer(Player p) {

		for(Team t : teams) {
			Config cfg = new Config(SUM.getInstance(), t.getName(), false);
			if(cfg.getConfig().getStringList("Players").contains(p.getName())
					|| p.getName().equals(cfg.getString("Owner"))) {
				return t;
			}
		}
		return null;
	}

	public Team getTeamByName(String name) {

		for(Team t : teams) {
			if(t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	public Team getTeamByOwner(Player p) {

		for(Team t : teams) {
			if(p.getName().equals(t.getOwner())) {
				return t;
			}
		}
		return null;
	}

	public Set<Team> getTeams() {
		return teams;
	}

}
