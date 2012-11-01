package com.timvisee.manager.economymanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import com.timvisee.manager.economymanager.EconomyManager;

public class WaitForIt implements Listener {
	EconomyManager manager = null;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent e){
		
	}
	
}