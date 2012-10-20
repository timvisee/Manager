package com.timvisee.manager.permissionsmanager;

public enum PermissionsManagerPermissionsSystemType {
	NONE("None"),
	PERMISSIONS_EX("Permissions Ex"),
	PERMISSIONS_BUKKIT("Permissions Bukkit"),
	B_PERMISSIONS("bPermissions"),
	ESSENTIALS_GROUP_MANAGER("Essentials Group Manager"),
	VAULT("Vault"),
	PERMISSIONS("Permissions");
	
	public String name;
	
	PermissionsManagerPermissionsSystemType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
