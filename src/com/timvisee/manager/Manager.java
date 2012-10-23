package com.timvisee.manager;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.timvisee.manager.economymanager.EconomyManager;
import com.timvisee.manager.permissionsmanager.PermissionsManager;

public class Manager extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");

	// Permissions and Economy manager
	private PermissionsManager pm;
	private EconomyManager em;
	
	public void onEnable() {
		// Setup the managers
		setupPermissionsManager();
		setupEconomyManager();
		
		// Plugin started
		
		log.info("[Manager] Manager Started");
	}

	public void onDisable() {
		log.info("[Manager] Manager Disabled");
	}
	
	/**
	 * Setup the economy manager
	 */
	public void setupEconomyManager() {
		// Setup the economy manager
		this.em = new EconomyManager(this.getServer(), this);
		this.em.setup();
	}
	
	/**
	 * Get the economy manager
	 * @return economy manager
	 */
	public EconomyManager getEconomyManager() {
		return this.em;
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setupPermissionsManager() {
		// Setup the permissions manager
		this.pm = new PermissionsManager(this.getServer(), this);
		this.pm.setup();
	}
	
	/**
	 * Get the permissions manager
	 * @return permissions manager
	 */
	public PermissionsManager getPermissionsManager() {
		return this.pm;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return false;
	}
}
