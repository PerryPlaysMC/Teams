package me.perryplaysmc.sumteams.Utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("all")
public class Config {

	private YamlConfiguration config;
	private File file;
	private Plugin plugin;
	private static Config instance;

	public Config(Plugin plugin, String FileName, boolean copy) {
		instance = this;
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder(), FileName + ".yml");

		if(copy) {

			plugin.saveResource(file.getName(), true);

		} else {

			try {
				file.createNewFile();
			} catch (Exception e) {

				e.printStackTrace();

			}

		}
		reloadConfig();

	}
	
	public String getString(String path) {
		return config.getString(path);
	}
	
	 public boolean getBoolean(String path) { return config.getBoolean(path); }
	 public boolean getBoolean(String path, boolean is) { return config.getBoolean(path, is); }
	 public Object get(String path) {return this.config.get(path);}
	 public Object get(String path, Object def) {return this.config.get(path, def);}
	
	public void set(String path, Object obj) {
		config.set(path, obj);
		saveConfig();
	}
	
	public YamlConfiguration getConfig() {
		return config;
	}

	public File getFile() {
		return file;
	}
	
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
	}

	public void saveConfig() {
		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Config getInstance() {
		return instance;
	}
	
}
