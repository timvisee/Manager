package com.timvisee.manager.economymanager;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;


// Economy Systems
import com.timvisee.SimpleEconomy.SimpleEconomy; // SimpleEconomy
import com.timvisee.SimpleEconomy.SimpleEconomyHandler.SimpleEconomyHandler;    // SimpleEconomy
import cosine.boseconomy.BOSEconomy;            // BOSEconomy
import com.earth2me.essentials.Essentials;      // Essentials
import com.earth2me.essentials.Settings;        // Essentials
import com.earth2me.essentials.api.NoLoanPermittedException;  // Essentials
import com.earth2me.essentials.api.UserDoesNotExistException; // Essentials
import me.mjolnir.mineconomy.internal.MCCom;     // MineConomy
import me.ethan.eWallet.ECO;                     // eWallet
import ca.agnate.EconXP.EconXP;                  // EconXP
import is.currency.Currency;                     // CurrencyCore
import com.greatmancode.craftconomy3.Common;                   // CraftConomy
import com.greatmancode.craftconomy3.currency.CurrencyManager; // CraftConomy
import org.neocraft.AEco.AEco;                  // AEco
import java.lang.reflect.Method;                // AEco

// Vault
import net.milkbowl.vault.economy.Economy;

public class EconomyManager {
	public enum EconomySystemType {
		NONE("None"),
		SIMPLE_ECONOMY("Simple Economy"),
		BOSECONOMY("BOSEconomy"),
		ESSENTIALS("Essentials"),
		MINECONOMY("MineConomy"),
		EWALLET("eWallet"),
		ECONXP("EconXP"),
		CURRENCYCORE("CurrencyCore"),
		CRAFTCONOMY("CraftConomy"),
		AECO("AEco"),
		VAULT("Vault");
		
		public String name;
		
		EconomySystemType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	private EconomySystemType economyType = EconomySystemType.NONE;
	private Server s;
	private Plugin p;

	// Simple Economy
	private static SimpleEconomyHandler simpleEconomyHandler;
	
	// BOSEconomy
	private BOSEconomy BOSEcon = null;
	
	// Essentials
	private Settings essConf = null;
	
	// eWallet
	private ECO eWallet = null;
	
	// EconXP
	private EconXP econXP = null;
	
	// CurrencyCore
	private Currency currencyC = null;
	
	// AEco
	private org.neocraft.AEco.part.Economy.Economy AEconomy = null;
	private Method AEwallet = null;
	
	// Vault
    public static net.milkbowl.vault.economy.Economy vaultEconomy = null;
	
	/**
	 * Constructor
	 * @param s server
	 * @param p this plugin
	 */
	public EconomyManager(Server s, Plugin p) {
		this.s = s;
		this.p = p;
	}
	
	/**
	 * Get the used economy system where the economy manager is hooked into
	 * @return economy system
	 */
	public EconomySystemType getUsedEconomySystemType() {
		return this.economyType;
	}
	
	/**
	 * Check if the economy manager hooked into any of the supported economy systems
	 * @return false if there isn't any economy system used
	 */
	public boolean isEnabled() {
		return !economyType.equals(EconomySystemType.NONE);
	}
	
	/**
	 * Check if the current economy system support banks
	 * @return true if supported
	 */
	public boolean hasBankSupport() {
		if(!isEnabled()) {
			// Not hooked into any permissions system, return false
			return false;
		}
		
		switch (this.economyType) {
		case SIMPLE_ECONOMY:
		case NONE:
			// Simple Economy
			// This system doesn't have support for banks
			return false;
			
		case BOSECONOMY:
			// BOSEconomy
			// This system does have support for banks
			return true;
			
		case ESSENTIALS:
			// Essentials
			// This system doesn't have support for banks
			return false;
			
		case EWALLET:
			// eWallet
			// This system doesn't have support for banks
			return false;
			
		case ECONXP:
			// EconXP
			// This system doesn't have support for banks
			return false;
			
		case CURRENCYCORE:
			// CurrencyCore
			// This system doesn't have support for banks
			return false;
			
		case CRAFTCONOMY:
			// CraftConomy
			// This system does have support for banks
			return true;
			
		case AECO:
			// AEco
			// This system doesn't have support for banks
			return false;
			
		case VAULT:
			// Vault
			return vaultEconomy.hasBankSupport();
			
		default:
			// Something went wrong, return false to prevent problems
			return false;
		}
	}
	
	/**
	 * Setup the economymanager and hook into the the active economy plugin.
	 * @return EconomySystemType the economy system hooked into.
	 */	
	public EconomySystemType setup() {
		// Define the plugin manager
		final PluginManager pm = this.s.getPluginManager();
		
		// Reset used economy system type
		economyType = EconomySystemType.NONE;
		
		// Boolean to tell if we are waiting for a plugin to enable
		Boolean waiting = false;
		
		// Check if Simple Economy is available
		Plugin simpleEconomy = pm.getPlugin("Simple Economy"); //TODO Rename plugin without space when updated
		if(simpleEconomy != null) {
			simpleEconomyHandler = ((SimpleEconomy) simpleEconomy).getHandler();
		    System.out.println("[" + p.getName() + "] Hooked into Simple Economy!");
		    return economyType = EconomySystemType.SIMPLE_ECONOMY;
		}
		
		// Check if BOSEconomy is available
	    Plugin bose = pm.getPlugin("BOSEconomy");
	    if(bose != null) {
	        BOSEcon = (BOSEconomy)bose;
			System.out.println("[" + p.getName() + "] Hooked into BOSEconomy!");
		    return economyType = EconomySystemType.BOSECONOMY;
	    }
	    
	    // Check if Essentials is available
	    Plugin essP = pm.getPlugin("Essentials");
	    if (essP != null){
	    	if(essP.isEnabled()){
		    	essConf = (Settings) ((Essentials) essP).getSettings();
		    	System.out.println("[" + p.getName() + "] Hooked into Essentials!");
		    	return economyType = EconomySystemType.ESSENTIALS;
	    	} else {
	    		System.out.println("[" + p.getName() + "] Waiting for Essentials to enable.");
				Bukkit.getServer().getPluginManager().registerEvents(new WaitForIt(this, essP), this.p);
				waiting = true;
	    	}
	    }
	    
	    // Check if MineConomy is available
	    Plugin mineConomy = pm.getPlugin("MineConomy");
	    if (mineConomy != null){
	    	System.out.println("[" + p.getName() + "] Hooked into MineConomy!");
	    	return economyType = EconomySystemType.MINECONOMY;
	    }
	    
	    // Check if eWallet is available
	    Plugin eWalletP = pm.getPlugin("eWallet");
	    if (eWalletP != null){
	    	eWallet = (ECO) eWalletP;
	    	System.out.println("[" + p.getName() + "] Hooked into eWallet!");
	    	return economyType = EconomySystemType.EWALLET;
	    }
	    
	    
	    // Check if EconXP is available
	    Plugin econXPlugin = pm.getPlugin("EconXP");
	    if (econXPlugin != null){
	    	econXP = (EconXP) econXPlugin;
	    	System.out.println("[" + p.getName() + "] Hooked into EconXP!");
	    	return economyType = EconomySystemType.ECONXP;
	    }
	    
	    // Check if CurrencyCore is available
	    Plugin currencyP = pm.getPlugin("CurrencyCore");
	    if (currencyP != null){
	    	currencyC = (Currency) currencyP;
	    	System.out.println("[" + p.getName() + "] Hooked into CurrencyCore!");
	    	return economyType = EconomySystemType.CURRENCYCORE;
	    }
	    
	    // Check if CraftConomy is available
	    Plugin craftConomyP = pm.getPlugin("Craftconomy3");
	    if (craftConomyP != null){
	    	System.out.println("[" + p.getName() + "] Hooked into CraftConomy!");
	    	return economyType = EconomySystemType.CRAFTCONOMY;
	    }
	    
	    // Check if AEco is available
	    Plugin AEcoP = pm.getPlugin("AEco");
	    if (AEcoP != null){
	    	Boolean error = false;
	    	if(AEcoP.isEnabled()){
	    		try {
		    		AEconomy = AEco.ECONOMY;
		    		AEwallet = AEconomy.getClass().getMethod("AEwallet", String.class);
		            AEwallet.setAccessible(true);
		    	} catch (SecurityException e) {
	            	error = true;
	            } catch (NoSuchMethodException e) {
	            	error = true;
	            }
		    	if(!error){
			    	System.out.println("[" + p.getName() + "] Hooked into AEco!");
			    	return economyType = EconomySystemType.AECO;
		    	}
	    	} else {
	    		System.out.println("[" + p.getName() + "] Waiting for AEco to enable.");
				Bukkit.getServer().getPluginManager().registerEvents(new WaitForIt(this, AEcoP), this.p);
				waiting = true;
	    	}	    	
	    }
	    
		// Check if Vault is available
	    final Plugin vaultPlugin = pm.getPlugin("Vault");
		if (vaultPlugin != null && vaultPlugin.isEnabled()) {
			RegisteredServiceProvider<Economy> economyProvider = this.s.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            vaultEconomy = economyProvider.getProvider();
	            if(vaultEconomy.isEnabled()) {
	            	System.out.println("[" + p.getName() + "] Hooked into Vault Economy!");
	    		    return economyType = EconomySystemType.VAULT;
	            } else
	            	System.out.println("[" + p.getName() + "] Not using Vault Economy, Vault Economy is disabled!");
	        }
		}
		
		// No recognized economy system found and not waiting for one to enable
		if(!waiting){
			System.out.println("[" + p.getName() + "] No supported economy system found! Economy disabled!");
		    return EconomySystemType.NONE;
		}
		
		// Set the economyType to none to avoid problems while waiting.
		return economyType = EconomySystemType.NONE;
		
	}
	
	/**
	 * Setup the economy system if we had to wait for it to enable
	 * @param ecoP EconomyPlugin that was enabled
	 */
	public void delayedSetup(Plugin ecoP){
		String ecoSysType = ecoP.getName();
		EconomySystemType ecoSystems[] = EconomySystemType.values();
		for (EconomySystemType ecoName: ecoSystems){
			if (ecoSysType.equalsIgnoreCase(ecoName.getName())){
				economyType = ecoName;
				switch(ecoName){
					case ESSENTIALS:
						essConf = (Settings) ((Essentials) ecoP).getSettings();
						break;
						
					case AECO:
						try {
							this.AEconomy = AEco.ECONOMY;
				    		this.AEwallet = AEconomy.getClass().getMethod("AEwallet", String.class);
				            this.AEwallet.setAccessible(true);
						} catch (SecurityException e) {
			            } catch (NoSuchMethodException e) {}
						break;
					default:
						break;
				}
				System.out.println("[" + this.p.getName() + "] Hooked into " + ecoP.getName() + "!");
				break;
			}
		}		
	}
	
	/**
	 * Get the money balance of a player
	 * @param p player
	 * @return money balance
	 */
	public double getBalance(Player p) {
		return getBalance(p.getName(), 0.00);
	}
	
	/**
	 * Get the money balance of a player
	 * @param p player
	 * @return money balance
	 */
	public double getBalance(Player p, double def) {
		return getBalance(p.getName(), def);
	}
	
	/**
	 * Get the money balance of a player
	 * @param p player name
	 * @return money balance
	 */
	public double getBalance(String p) {
		return getBalance(p, 0.00);
	}
	
	/**
	 * Get the money balance of a player
	 * @param p player name
	 * @param def default balance if not hooked into any economy system
	 * @return money balance
	 */
	public double getBalance(String p, double def) {
		if(!isEnabled()) {
			// No economy system is used, return zero balance
			return 0.00;
		}
		
		switch(this.economyType) {
		case SIMPLE_ECONOMY:
			// Simple Economy
			return simpleEconomyHandler.getMoney(p);
			
		case BOSECONOMY:
			// BOSEconomy
			return BOSEcon.getPlayerMoneyDouble(p);
			
		case ESSENTIALS:
			// Essentials
			try {
				return com.earth2me.essentials.api.Economy.getMoney(p);
			} catch (UserDoesNotExistException e) {
				com.earth2me.essentials.api.Economy.createNPC(p);
				return 0.00;
			}
			
		case MINECONOMY:
			// MineConomy
	        return MCCom.getExternalBalance(p);
	        
		case EWALLET:
			// eWallet
			Integer eWB = eWallet.getMoney(p);
			return (Double) (eWB == null ? 0.00 : eWB);
	        
		case ECONXP:
			// EconXP
			return econXP.getExp(econXP.getPlayer(p));
			
		case CURRENCYCORE:
			// CurrencyCore
			return currencyC.getAccountManager().getAccount(p).getBalance();
			
		case CRAFTCONOMY:
			// CraftConomy
			return Common.getInstance().getAccountManager().getAccount(p).getBalance(
						Common.getInstance().getServerCaller().getDefaultWorld(), // The default world
						Common.getInstance().getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName() // The default currency
				   );
	       
		case AECO:
			// AEco
			return AEconomy.cash(p);
			
		case VAULT:
			// Vault
			return vaultEconomy.getBalance(p);
			
		case NONE:
			// Not hooked into any economy system, return default balance
			return def;
			
		default:
			// Something went wrong, return zero balance to prevent problems
			return 0.00;
		}
	}
	
	
	/**
	 * Check if a player has enough money balance to pay something
	 * @param p player
	 * @param price price to pay
	 * @return true if the player has enough money
	 */
	public boolean hasEnoughMoney(Player p, double price) {
		return hasEnoughMoney(p.getName(), price);
	}
	
	/**
	 * Check if a player has enough money balance to pay something
	 * @param p player name
	 * @param price price to pay
	 * @return true if the player has enough money
	 */
	public boolean hasEnoughMoney(String p, double price) {
		double balance = getBalance(p);
		return (balance >= price);
	}
	
	/**
	 * Deposit money to a player
	 * @param p player
	 * @param money money amount
	 * @return false when something was wrong
	 */
	public boolean depositMoney(Player p, double money) {
		return depositMoney(p.getName(), money);
	}
	
	/**
	 * Deposit money to a player
	 * @param p player name
	 * @param money money amount
	 * @return false when something was wrong
	 */
	public boolean depositMoney(String p, double money) {
		if(!isEnabled()) {
			// No economy system is used, return false
			return false;
		}
		
		// Get current player balance
		//double balance = getBalance(p);
		
		// Deposit money
		switch(this.economyType) {
		case SIMPLE_ECONOMY:
			// Simple Economy
			simpleEconomyHandler.addMoney(p, money);
			break;
			
		case BOSECONOMY:
			// BOSEconomy
			BOSEcon.addPlayerMoney(p, money, false);
			break;
		
		case ESSENTIALS:
			// Essentials
			try {
				com.earth2me.essentials.api.Economy.add(p, money);
			} catch (UserDoesNotExistException e) {
				if(com.earth2me.essentials.api.Economy.createNPC(p)){
					return depositMoney(p, money);
				} else 
					return false;
			} catch (NoLoanPermittedException e) {
				return false;
			}
			break;
			
		case MINECONOMY:
			// MineConomy
            MCCom.setExternalBalance(p, MCCom.getExternalBalance(p)+money);
			break;
			
		case EWALLET:
			// eWallet
			eWallet.giveMoney(p, (int) Math.ceil(money));
			break;
			
		case ECONXP:
			// EconXP
			econXP.addExp(econXP.getPlayer(p), (int)Math.ceil(money));
			break;
			
		case CURRENCYCORE:
			// CurrencyCore
			currencyC.getAccountManager().getAccount(p).addBalance(money);
			break;
		
		case CRAFTCONOMY:
			// CraftConomy
			Common.getInstance().getAccountManager().getAccount(p).deposit(
				money,
				Common.getInstance().getServerCaller().getDefaultWorld(), // The default world
				Common.getInstance().getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName() // The default currency
			);
			break;
			   
		case AECO:
			// AEco
			AEconomy.add(p, (int) Math.ceil(money));
			break;
						
		case VAULT:
			// Vault
			vaultEconomy.depositPlayer(p, money);
			break;
			
		case NONE:
			// Not hooked into any economy system, return false
			return false;
			
		default:
			// Something went wrong, return false to prevent problems
			return false;
		}
		
		return true;
	}
	
	/**
	 * Withdraw money from a player
	 * @param p player
	 * @param money money amount
	 * @return false when something was wrong
	 */
	public boolean withdrawMoney(Player p, double money) {
		return withdrawMoney(p.getName(), money);
	}
	
	/**
	 * Withdraw money from a player
	 * @param p player name
	 * @param money money amount
	 * @return false when something was wrong
	 */
	public boolean withdrawMoney(String p, double money) {
		if(!isEnabled()) {
			// No economy system is used, return false
			return false;
		}
		
		// Get current player balance
		double balance = getBalance(p);
		double newBalance = balance - money;
		
		// The new Balance has to be zero or above
		if(newBalance < 0) {
			return false;
		}
		
		// Withdraw money
		switch(this.economyType) {
		case SIMPLE_ECONOMY:
			// Simple Economy
			simpleEconomyHandler.subtractMoney(p, money);
			break;
			
		case BOSECONOMY:
			// BOSEconomy
			BOSEcon.setPlayerMoney(p, newBalance, false);
			break;
		
		case ESSENTIALS:
			// Essentials
			try {
				com.earth2me.essentials.api.Economy.subtract(p, money);
			} catch (UserDoesNotExistException e) {
				if(com.earth2me.essentials.api.Economy.createNPC(p)){
					return withdrawMoney(p, money);
				} else 
					return false;
			} catch (NoLoanPermittedException e) {
				return false;
			}
			break;
			
		case MINECONOMY:
			// MineConomy
			MCCom.setExternalBalance(p, MCCom.getExternalBalance(p)-money);
			break;
		
		case EWALLET:
			// eWallet
			eWallet.takeMoney(p, (int) Math.ceil(money));
			break;
			
		case ECONXP:
			// EconXP
			econXP.removeExp(econXP.getPlayer(p), (int)Math.ceil(money));
			break;
			
		case CURRENCYCORE:
			// CurrencyCore
			currencyC.getAccountManager().getAccount(p).subtractBalance(money);
			break;
			
			case CRAFTCONOMY:
				// CraftConomy
				Common.getInstance().getAccountManager().getAccount(p).withdraw(
					money,
					Common.getInstance().getServerCaller().getDefaultWorld(), // The default world
					Common.getInstance().getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName() // The default currency
				);
				break;
				   
		case AECO:
			// AEco
			AEconomy.remove(p, (int) Math.ceil(money));
			break;
							
		case VAULT:
			// Vault
			vaultEconomy.withdrawPlayer(p, money);
			break;
			
		case NONE:
			// Not hooked into any economy system, return false
			return false;
			
		default:
			// Something went wrong, return false to prevent problems
			return false;
		}
		
		return true;
	}
	
	/**
	 * Get the currency name
	 * @param money the current balance (to get the Singular/Plural thingy right)
	 * @return currency name
	 */
	public String getCurrencyName(double money) {
		return getCurrencyName(money, "Money");
	}
	
	/**
	 * Get the currency name
	 * @param money the current balance (to get the Singular/Plural thingy right)
	 * @param def the default currency name
	 * @return currency name
	 */
	public String getCurrencyName(double money, String def) {
		if(!isEnabled()) {
			// No economy system is used, return false
			return def;
		}
		
		// Get currency name
		switch(this.economyType) {
		case SIMPLE_ECONOMY:
			// Simple Economy
			//TODO Finish this function in the API of Simple Economy
			return "Silver";
			
		case BOSECONOMY:
			// BOSEconomy
			return BOSEcon.getMoneyNameProper(money);
			
		case ESSENTIALS:
			// Essentials
			return "";

		case MINECONOMY:
			// MineConomy
			return MCCom.getDefaultCurrency();
		
		case EWALLET:
			// eWallet
			if(money == 1){
				return eWallet.singularCurrency;
			} else {
				return eWallet.pluralCurrency;
			}
			
		case ECONXP:
			// EconXP
			return "experience";
			
		case CURRENCYCORE:
			// CurrencyCore
			if(money == 1) {
				return currencyC.getCurrencyConfig().getCurrencyMajor().get(0);
			} else {
				return currencyC.getCurrencyConfig().getCurrencyMajor().get(1);
			}
			
		case CRAFTCONOMY:
			// CraftConomy
			if(money == 1) {
				return Common.getInstance().getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName();
			} else {
				return Common.getInstance().getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getPlural();
			}
		
		case AECO:
			// AEco
			return "";
			
		case VAULT:
			// Vault
			if(money == 1) {
				return vaultEconomy.currencyNameSingular();
			} else {
				return vaultEconomy.currencyNamePlural();
			}
			
		case NONE:
			// Not hooked into any economy system, return false
			return def;
			
		default:
			// Something went wrong, return false to prevent problems
			return def;
		}
	}
	
	/**
	 * Get the currency symbol
	 * @return currency symbol
	 */
	public String getCurrencySymbol() {
		return getCurrencySymbol("$");
	}
	
	/**
	 * Get the currency symbol
	 * @param def the default currency symbol
	 * @return currency symbol
	 */
	public String getCurrencySymbol(String def) {
		if(!isEnabled()) {
			// No economy system is used, return false
			return def;
		}
		
		// Get currency symbol
		switch(this.economyType) {
		case SIMPLE_ECONOMY:
			// Simple Economy
			return "";
			
		case BOSECONOMY:
			// BOSEconomy
			return "";
			
		case ESSENTIALS:
			// Essentials
			return essConf.getCurrencySymbol();

		case MINECONOMY:
			// MineConomy
			return "";
		
		case EWALLET:
			// eWallet
			return "";
			
		case ECONXP:
			// EconXP
			return "";
			
		case CURRENCYCORE:
			// CurrencyCore
			return "";
			
		case CRAFTCONOMY:
			// CraftConomy
			return "";
		
		case AECO:
			// AEco
			return AEco.CONFIG.getCurrency();
			
		case VAULT:
			// Vault
			return "";
			
		case NONE:
			// Not hooked into any economy system, return false
			return def;
			
		default:
			// Something went wrong, return false to prevent problems
			return def;
		}
	}
	
	
	/**
	 * The WaitForIt class is exactly what the name suggests, it waits for a plugin to enable.
	 * When the plugin enables it calls the delayedSetup method of the EconomyManager class to hook into the enabled plugin.
	 */
	public class WaitForIt implements Listener {
		EconomyManager manager = null;
		Plugin eco = null;
		
		public WaitForIt(EconomyManager manager, Plugin eco) {
	        this.manager = manager;
	        this.eco = eco;
	    }
		
		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent e){
			if (eco != null && eco.isEnabled()){
				manager.delayedSetup(eco);
			}
        }
	}
	
}