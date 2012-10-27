package com.timvisee.manager.economymanager;

public enum EconomySystemType {
	NONE("None"),
	SIMPLE_ECONOMY("Simple Economy"),
	BOSECONOMY("BOSEconomy"),
	ICONOMY6("iConomy6"),
	VAULT("Vault");
	
	public String name;
	
	EconomySystemType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
