package me.perryplaysmc.sumteams;

import org.bukkit.plugin.java.JavaPlugin;

import me.perryplaysmc.sumteams.Teams.Team;
import me.perryplaysmc.sumteams.Teams.TeamCommand;
import me.perryplaysmc.sumteams.Teams.TeamCompleter;
import me.perryplaysmc.sumteams.Teams.TeamManager;
import me.perryplaysmc.sumteams.Utils.Config;

public class SUM extends JavaPlugin {

	private static SUM instance;
	private TeamManager m;
	private Config cf;

	@Override
	public void onEnable() {
		instance = this;
		m = new TeamManager();
		loadTeams();
		getCommand("team").setExecutor(new TeamCommand());
		getCommand("team").setTabCompleter(new TeamCompleter());
		getServer().getPluginManager().registerEvents(new TeamCommand(), this);
		saveDefaultConfig();
	}

	@Override
	public void onDisable() {

	}

	private void loadTeams() {
		if(getConfig().getStringList("ConfigNames") != null) {
			for(String a : getConfig().getStringList("ConfigNames")) {
				cf = new Config(this, a, false);
				
			}if(cf != null) {
					if(cf.get("Name") != null) {
						new Team(cf.getString("Name"), cf.getString("Owner"));
					}
				}
		}

	}
	public TeamManager getManager() {
		return m;
	}

	public static SUM getInstance() {
		return instance;
	}


}
