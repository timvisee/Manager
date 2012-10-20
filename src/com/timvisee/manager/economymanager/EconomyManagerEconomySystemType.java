package com.timvisee.manager.economymanager;

public enum EconomyManagerEconomySystemType {
	NONE("None"),
	SIMPLE_ECONOMY("Simple Economy"),
	BOSECONOMY("BOSEconomy"),
	VAULT("Vault");
	
	public String name;
	
	EconomyManagerEconomySystemType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
