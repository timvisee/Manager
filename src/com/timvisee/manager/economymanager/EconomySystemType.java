package com.timvisee.manager.economymanager;

public enum EconomySystemType {
	NONE("None"),
	SIMPLE_ECONOMY("Simple Economy"),
	BOSECONOMY("BOSEconomy"),
	MINECONOMY("MineConomy"),
	VAULT("Vault");
	
	public String name;
	
	EconomySystemType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
