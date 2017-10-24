package me.perryplaysmc.sumteams.Teams;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.perryplaysmc.sumteams.SUM;
import me.perryplaysmc.sumteams.Utils.Config;

@SuppressWarnings("all")
public class Team {

	private SUM st = SUM.getInstance();
	private List<String> pit;
	private String name;
	private String owner;

	public Team(String name, String owner) {
		Config cfg = new Config(SUM.getInstance(), name, false);
		this.name = name;
		this.owner = owner;
		this.pit = cfg.getConfig().getStringList("Players");
		st.getManager().addTeam(this);
	}

	public boolean isOwner(Player p) {
		Config cfg = new Config(SUM.getInstance(), getName(), false);
		if(p.getName().equals(cfg.getString("Owner"))) {
			return true;
		}
		return false;
	}

	public void delete() {
		BroadCast("Your team has been deleted!");
		Config cfg = new Config(SUM.getInstance(), getName(), false);
		cfg.getFile().delete();
		List<String> cfgs = st.getConfig().getStringList("ConfigFiles");
		cfgs.remove(getName());
		st.getConfig().set("ConfigNames", cfgs);
		st.saveConfig();
		pit.clear();
		st.getManager().removeTeam(this);
	}

	public void BroadCastNOOWNER(String... msgs) {
		Config cfg = new Config(SUM.getInstance(), getName(), false);
		if(getPlayers() != null) {
			for(String p : getPlayers()) {
				for(String a : msgs) {
					if(Bukkit.getPlayer(p) != null) {
						Bukkit.getPlayer(p).sendMessage(("&1&lSUM&4&lTeams&c: " + a + "\n").trim().replace("&", "§"));
					}
				}
			}
		}
	}
	
	public void BroadCast(String... msgs) {
		Config cfg = new Config(SUM.getInstance(), getName(), false);
		if(getPlayers() != null) {
			for(String p : getPlayers()) {
				for(String a : msgs) {
					if(Bukkit.getPlayer(p) != null) {
						Bukkit.getPlayer(p).sendMessage(("&1&lSUM&4&lTeams&c: " + a + "\n").trim().replace("&", "§"));
					}
				}
			}
		}
		for(String a : msgs) {
			if(Bukkit.getPlayer(cfg.getString("Owner")) != null) {
				Bukkit.getPlayer(cfg.getString("Owner")).sendMessage(("&1&lSUM&4&lTeams&c: " + a + "\n").trim().replace("&", "§"));
			}
		}
	}

	public List<String> getPlayers() {
		Config cfg = new Config(SUM.getInstance(), getName(), false);
		return cfg.getConfig().getStringList("Players");
	}

	public String getOwner() {
		Config cfg = new Config(SUM.getInstance(), getName(), false);
		return cfg.getConfig().getString("Owner");
	}

	public String getName() {
		return name;
	}


}
