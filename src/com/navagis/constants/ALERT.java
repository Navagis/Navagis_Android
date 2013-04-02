package com.navagis.constants;

public enum ALERT {
	TITLE_DEVICE_OFF("Device shutting down"),
	TITLE_NO_MOVEMENT ("Asset not moving"),
	TITLE_DEVIATE_PATH ("Asset has deviated path"),
	EMAIL ("reliance.pilot@gmail.com")
	;
	
	final String title;
	ALERT(String title) {
		this.title = title;
	}
	
	public String toString() {
		return this.title;
	}
	
	public enum MESSAGE {
		DEVICE_OFF("Device is Shutting down"),
		NO_MOVEMENT ("Asset is not moving"),
		DEVIATE_PATH ("Asset has deviated path"),
		;
		
		final String message;
		MESSAGE(String title) {
			this.message = title;
		}
		
		public String toString() {
			return this.message;
		}
	}
}
