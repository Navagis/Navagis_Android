package com.navagis.constants;

public enum SERVER {
	LOCAL 	("localhost"),
	DEV		("reliance@cloudcontrolled.com"),
	PROD	("evm.navagis.com/AssetTrackingDemo/")
	;
	
	final String ipAddress;
	SERVER(String ip) {
		this.ipAddress = ip;
	}
	
	@Override
	public String toString() {
		return this.ipAddress;
	}
}
