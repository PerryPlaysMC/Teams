package me.perryplaysmc.sumteams.Teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.perryplaysmc.sumteams.SUM;
import me.perryplaysmc.sumteams.Utils.Config;
import me.perryplaysmc.sumteams.Utils.Utils;

public class TeamCommand implements CommandExecutor, Listener, Utils {

	private SUM st = SUM.getInstance();
	private HashMap<Player, Team> add = new HashMap<>();
	private Player p;
	private Team t;
	private Config cfg;
	public static String OWNER;
	@Override
	public boolean onCommand(CommandSender s, Command ccccc, String cl, String[] args) {
		if(!(s instanceof Player))
		{
			s.sendMessage("Only players may use this command.");
			return true;
		}
		p = (Player) s;
		if(cl.equalsIgnoreCase("team")) {
			if(!s.hasPermission("sumteams.command")) {
				sendMessage(s, "You do not have permission for this command!");
				return true;
			}
			if(args.length == 0) {
				sendMessage(p, "Superheroes Unlimited Teams Commands:"
						, "/team &bjoin &c: Join a team! &6: Alias: accept"
						, "/team &bleave &c: Leave your team for your own reasons!"
						, "/team &bchat &c: Toggle your team chat!"
						, "/team &bdelete &c: Delete your team! &6: Alias: disband"
						, "/team &bcreate <name> &c: Create a team!"
						, "/team &binvite <Player> &c: Add a player to your team! &6: Alias: add"
						, "/team &bkick <Player> &c: Remove a player from your team! &6: Alias: remove"
						, "/team &bsetowner <Player> &c: Set your team leader to someone else!");
				return true;
			}else if(args.length == 1 && !args[0].equalsIgnoreCase("leave")
					&& !args[0].equalsIgnoreCase("chat") && !args[0].equalsIgnoreCase("delete")
					&& !args[0].equalsIgnoreCase("disband")
					&& !args[0].equalsIgnoreCase("accept")
					&& !args[0].equalsIgnoreCase("join")
					&& !args[0].equalsIgnoreCase("reload")
					&& !args[0].equalsIgnoreCase("status")) {
				sendMessage(p, "Superheroes Unlimited Teams Commands:"
						, "/team &bjoin &c: Join a team! &6: Alias: accept"
						, "/team &bstatus &c: Check the status of your team!"
						, "/team &bleave &c: Leave your team for your own reasons!"
						, "/team &bchat &c: Toggle your team chat!"
						, "/team &bdelete &c: Delete your team! &6: Alias: disband"
						, "/team &bcreate <name> &c: Create a team!"
						, "/team &binvite <Player> &c: Add a player to your team! &6: Alias: add"
						, "/team &bkick <Player> &c: Remove a player from your team! &6: Alias: remove"
						, "/team &bsetowner <Player> &c: Set your team leader to someone else!");
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(!s.hasPermission("sumteams.reload")) {
						sendMessage(s, "You do not have permission for this command!");
						return true;
					}
					st.reloadConfig();
					sendMessage(s, "Config successfully reloaded!");
					return true;
				}
				if(args[0].equalsIgnoreCase("status")) {
					t = st.getManager().getTeamByPlayer(p);
					if(t != null) {
						cfg = new Config(st, t.getName(), false);
						List<String> on = new ArrayList<String>();
						List<String> off = new ArrayList<String>();
						for(String a : t.getPlayers()) {
							@SuppressWarnings("deprecation")
							OfflinePlayer c = Bukkit.getOfflinePlayer(a);
							if(c.isOnline()) {
								on.add(c.getName());
							}else {
								off.add(c.getName());
							}
						}
						String status = "Bad";
						sendMessage(s, "Your team status"
								, "Name: " + t.getName()
								, "Owner: &4&l" + t.getOwner()
								, "Players: "
								);
						if(!on.isEmpty() && !off.isEmpty()) {
							if(on.size() > 1) {
								status = "Good";
							}
							if(on.size() > 2) {
								status = "Great";
							}
							if(on.size() > 3) {
								status = "Amazing";
							}
							if(on.size() > 4) {
								status = "Fantastic";
							}
							if(off.size() < 1) {
								status = "Fine";
							}
							if(off.size() < 2) {
								status = "Bad";
							}
							if(off.size() < 3) {
								status = "Terrible";
							}
							if(off.size() < 4) {
								status = "Horrible";
							}
						}
						if(!on.isEmpty() && !off.isEmpty()) {
							sendMessage(s
									, "Online (&4&l" + on.size() + "&r&c): "
									, "&4&l" + on.toString().replace("[", "").replace(",", "&r&c,&4&l,").replace("]", "")
									, "Offline (&4&l" + off.size() + "&r&c): "
									, "&4&l" + off.toString().replace("[", "").replace(",", "&r&c,&4&l,").replace("]", "")
									, "Status: " + status);
							return true;
						}
						else if(!on.isEmpty() && off.isEmpty()) {
							sendMessage(s
									, "Online (&4&l" + on.size() + "&r&c): "
									, "&4&l" + on.toString().replace("[", "").replace(",", "&r&c,&4&l,").replace("]", "")
									, "Status: " + status);
						}
						else if(!off.isEmpty() && on.isEmpty()) {
							sendMessage(s
									, "Offline (&4&l" + off.size() + "&r&c): "
									, "&4&l" + off.toString().replace("[", "").replace(",", "&r&c,&4&l,").replace("]", "")
									, "Status: " + status);
						}else {
							sendMessage(s, "None");
						}
					}else {	
						sendMessage(s, "Your team status"
								, "Owner: None"
								, "Players: None"
								, "Status: Bad, forever alone.");
					}
				}
				if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("disband")) {
					t = st.getManager().getTeamByOwner(p);
					if(t != null) {
						cfg = new Config(st, t.getName(), false);
						if(p.getName().equals(cfg.getString("Owner"))) {
							t.delete();
						}else {
							sendMessage(s, "You are not the owner!", "So you may not delete this team!");
						}
					}else {
						sendMessage(s, "You are not in a team!");
					}
				}
				else if(args[0].equalsIgnoreCase("leave")) {
					t = st.getManager().getTeamByPlayer(p);
					if(t != null) {
						cfg = new Config(st, t.getName(), false);
						if(!t.isOwner(p)) {
							List<String> pls = cfg.getConfig().getStringList("Players");
							pls.remove(p.getName());
							cfg.set("Players", pls);
							sendMessage(p, "You have left team: &4&l" + t.getName());
							t.BroadCast("&4&l" + p.getName() + "&c Has left your team!");
						}else {
							sendMessage(s, "You must set a different owner before you can leave!");
						}
					}else {
						sendMessage(s, "You are not in a team!");
					}
				}
				else if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("join")) {
					if(add.containsKey(p)) {
						t = add.get(p);
						cfg = new Config(st, t.getName(), false);
						List<String> cfgs = cfg.getConfig().getStringList("Players");
						cfgs.add(p.getName());
						cfg.getConfig().set("Players", cfgs);
						cfg.saveConfig();
						t.BroadCast("&4&l" + s.getName() + " &r&cHas joined your team!");
					}else {
						sendMessage(s, "You do not have any invites!");
					}
				}
				else if(args[0].equalsIgnoreCase("chat")) {
					t = st.getManager().getTeamByPlayer(p);
					if(t != null) {
						cfg = new Config(st, t.getName(), false);
						if(cfg.getConfig().get(p.getName()) == null) {
							cfg.set(p.getName(), true);	
							sendMessage(s, "TeamChat enabled.");
							return true;
						}
						if(cfg.getConfig().getBoolean(p.getName())) {
							cfg.set(p.getName(), false);	
							sendMessage(s, "TeamChat disabled.");
						}else {
							cfg.set(p.getName(), true);
							sendMessage(s, "TeamChat enabled.");
						}
					}else {
						sendMessage(s, "You are not in a team!");
					}
				}
				return true;
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(args[1].equalsIgnoreCase("all")) {
						if(!s.hasPermission("sumteams.reload.all")) {
							sendMessage(s, "You do not have permission for this command!");
							return true;
						}
						for(String a : st.getConfig().getStringList("ConfigNames")) {
							cfg = new Config(st, a, false);
						}
						st.reloadConfig();
						sendMessage(s, "Config successfully reloaded!");
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("disband")) {
					if(s.hasPermission("sumteams.override.remove")) {
						t = st.getManager().getTeamByName(args[1]);
						if(t != null) {
							cfg = new Config(st, t.getName(), false);
							t.delete();
							sendMessage(s, "You deleted Team: &4&l" + t.getName());
						}else {
							sendMessage(s, "That team doesn't exist!");
						}
					}else {
						sendMessage(s, "You do not have permission for this command!");
					}
				}
				else if(args[0].equalsIgnoreCase("create")) {
					if(st.getManager().getTeamByName(args[1]) != null) {
						sendMessage(s, "That team already exists!");
						return true;
					}
					Config cf = null;
					if(st.getConfig().getStringList("ConfigNames") != null) {
						for(String a : st.getConfig().getStringList("ConfigNames")) {
							cf = new Config(st, a, false);
						}
					}
					if(cf != null) { 
						if(cf.get("Name") != null) {
							if(cf.getConfig().getStringList("Players").contains(p.getName()) || cf.getString("Owner").equals(p.getName())) {
								sendMessage(s, "You're already on a team!");
								return true;
							}
						}
					}
					for(String a : st.getConfig().getStringList("DisabledWordsInName")) {
						if(args[1].contains(a)) {
							sendMessage(s, "You may not put: &4&l" + a + "&r&c in your team name!");
							return true;
						}
					}
					t = new Team(args[1], p.getName());
					cfg = new Config(st, t.getName(), false);
					cfg.set("Name", args[1]);
					cfg.set("Owner", p.getName());
					OWNER = t.getOwner();
					List<String> c = st.getConfig().getStringList("ConfigNames");
					c.add(t.getName());
					st.getConfig().set("ConfigNames", c);
					st.saveDefaultConfig();
					broadcast("&4&l" + s.getName() + " &r&cHas created the team: &4&l" + t.getName() + "&r&c!");
				}
				else if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("add")) {
					t = st.getManager().getTeamByPlayer(p);
					if(t != null) {
						if(t.isOwner(p)) {
							cfg = new Config(st, t.getName(), false);
							Player pt = Bukkit.getPlayer(args[1]);
							if(pt == null) {
								sendMessage(s, "That player is not online!");
								return true;
							}
							List<String> cfgs = cfg.getConfig().getStringList("Players");
							if(!cfgs.contains(pt.getName())) {
								if(pt.getName() == cfg.getString("Owner")) {
									sendMessage(s, "You are the owner of this team.", "So you may not send a invation to yourself!");
									return true;
								}
								sendMessage(pt, "&4&l" + s.getName() + "&r&c Has invited you to join their Team", "Do /team accept(join) To join their team!");
								add.put(pt, t);
								t.BroadCast("&4&l" + s.getName() + "&r&c Has invited: &4&l" + pt.getName() + "&r&c To your team!");
							}else {
								sendMessage(s, "That player is already on your team!");
							}
						}else {
							sendMessage(s, "You are not the team leader!", "So you may not add someone to your team!");
						}
					}else {
						sendMessage(s, "You are not in a team!");
					}
				}
				else if(args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("remove")) {
					t = st.getManager().getTeamByOwner(p);
					if(t != null) {
						cfg = new Config(st, t.getName(), false);
						@SuppressWarnings("deprecation")
						OfflinePlayer pt = Bukkit.getOfflinePlayer(args[1]);
						List<String> cfgs = cfg.getConfig().getStringList("Players");
						if(cfgs.contains(pt.getName())) {
							t.BroadCast("&4&l" + s.getName() + "&r&c Has kicked: &4&l" + pt.getName() + "&r&c. From your team!");
							cfgs.remove(pt.getName());
							cfg.set("Players", cfgs);
						}else {
							sendMessage(s, "That player isn't on your team!");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("setowner")) {
					t = st.getManager().getTeamByOwner(p);
					if(t != null) {
						cfg = new Config(st, t.getName(), false);
						Player pt = Bukkit.getPlayer(args[1]);
						if(pt != null) {
							if(t.getPlayers().contains(pt.getName())) {
								List<String> cfgs = cfg.getConfig().getStringList("Players");
								cfgs.remove(pt.getName());
								cfgs.add(s.getName());
								cfg.set("Players", cfgs);
								cfg.set("Owner", pt.getName());
								t.BroadCast(s.getName() + "&c Set Team Leader/Owner to &4&l" + pt.getName() + "&c.");
							}else {
								sendMessage(s, "That player isn't apart of your team!");
							}
						}else {
							sendMessage(s, "You are not in a team!");
						}
					}
				}else if(args.length == 2 && !args[0].equalsIgnoreCase("setowner")
						&& !args[0].equalsIgnoreCase("kick")
						&& !args[0].equalsIgnoreCase("invite")
						&& !args[0].equalsIgnoreCase("accept")) {
					sendMessage(p, "Superheroes Unlimited Teams Commands:"
							, "/team &bcreate <name> &c: Create a team!"
							, "/team &binvite <Player> &c: Add a player to your team! &6: Alias: add"
							, "/team &bleave &c: Leave your team for your own reasons!"
							, "/team &bkick <Player> &c: Remove a player from your team! &6: Alias: remove"
							, "/team &bchat &c: Toggle your team chat!"
							, "/team &bsetowner <Player> &c: Set your team leader to someone else!"
							, "/team &bdelete &c: Delete your team! &6: Alias: disband");
					return true;
				}
			}else if(args.length > 2) {
				sendMessage(p, "Superheroes Unlimited Teams Commands:"
						, "/team &bjoin &c: Join a team! &6: Alias: accept"
						, "/team &bleave &c: Leave your team for your own reasons!"
						, "/team &bchat &c: Toggle your team chat!"
						, "/team &bdelete &c: Delete your team! &6: Alias: disband"
						, "/team &bcreate <name> &c: Create a team!"
						, "/team &binvite <Player> &c: Add a player to your team! &6: Alias: add"
						, "/team &bkick <Player> &c: Remove a player from your team! &6: Alias: remove"
						, "/team &bsetowner <Player> &c: Set your team leader to someone else!");
				return true;
			}
		}
		return true;
	}

	@EventHandler
	void teamAttack(EntityDamageByEntityEvent e) {
		p = (Player) e.getDamager();
		t = st.getManager().getTeamByPlayer((Player) e.getEntity());
		if(t != null) {
			if(t.getPlayers().contains(((Player)e.getEntity()).getName()) || t.isOwner(((Player)e.getEntity()))) {
				e.setCancelled(true);
				sendMessage(p, "You can't attack &4&l" + ((Player)e.getEntity()).getName() + " &cBecause he/she is apart of your team!");
			}
		}
	}

	@EventHandler
	void onJoin(PlayerLoginEvent e) {
		t = st.getManager().getTeamByPlayer(e.getPlayer());
		if(t!=null) {
			t.BroadCast("&4&l" + e.getPlayer().getName() + "&c: Has joined!");
		}
	}

	@EventHandler
	void onQuit(PlayerQuitEvent e) {
		t = st.getManager().getTeamByPlayer(e.getPlayer());
		if(t!=null) {
			t.BroadCast("&4&l" + e.getPlayer().getName() + "&c: Has quit!");
		}
	}

	@EventHandler
	void onTeamChat(AsyncPlayerChatEvent e) {
		p = (Player) e.getPlayer();
		t = st.getManager().getTeamByPlayer(p);
		if(t != null) {
			Config cfg = new Config(st, t.getName(), false);
			if(cfg.get(p.getName()) != null) {
				if(cfg.getBoolean(p.getName())) {
					e.setCancelled(true);
					t.BroadCast("&4&l" + p.getName() + "&r&c: " + e.getMessage());
				}else {
					e.setCancelled(false);
				}
			}
		}
	}
}
