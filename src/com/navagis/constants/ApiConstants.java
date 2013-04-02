package com.navagis.constants;


public final class ApiConstants {
    // Keys in request packets
    public enum API_PACKET_REQUEST {
	TYPE				("type"),
	FORMAT				("format"),
	PACKET_ID			("packet_id"),
	DATA				("data"),
	MOBILE_DEVICE_ID	("mobile_device_id"),
	APP_VERSION			("app_version"),
	APP_PACKAGE			("app_package"),
	AUTH_TOKEN			("auth_token")
	;

	final String name;

	API_PACKET_REQUEST(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return this.name;
	}
    };
    
	 // Format types
    public enum API_PACKET_FORMAT {
	NORMAL		("normal"),
	GRID		("grid")
	;

	public final String name;

	API_PACKET_FORMAT(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return this.name;
	}

	public String getValue() {
	    return name;
	}

	public static API_PACKET_FORMAT fromName(String value) {
	    if (value != null) {
		API_PACKET_FORMAT[] types = API_PACKET_FORMAT.values();
		for (int i = 0; i < types.length; i++) {
		    if (value.equals(types[i].name)) {
			return types[i];
		    }
		}
	    }
	    throw new IllegalArgumentException();
	}
    };

    // Possible request packet types
    public enum API_PACKET_TYPE {
	ERROR					("error"),

	ADD_DEVICE				("addDevice"),
	ADD_ALERT				("addAlert"),
	LOG_IN					("logIn"),
	LOG_OUT					("logOut"),
	SEND_ASSET_TRACKING		("sendAssetTracking"),
	RETRIEVE_ALL_ASSETS		("retrieveAllAssets"), 
	SEND_PENDING_UPDATE		("sendPendingUpdate")
	;

	final String name;

	API_PACKET_TYPE(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return this.name;
	}

	public String getValue() {
	    return name;
	}

	public static API_PACKET_TYPE fromName(String value) {
	    if (value != null) {
		API_PACKET_TYPE[] types = API_PACKET_TYPE.values();
		for (int i = 0; i < types.length; i++) {
		    if (value.equals(types[i].name)) {
			return types[i];
		    }
		}
	    }
	    throw new IllegalArgumentException();
	}
    };

    // Data keys
    public enum API_PACKET_DATA {
	ID				("id"),
	NAME			("name"),
	RESULT			("result"),
	POINTS			("points"),
	GEOMETRY		("geometry"),
	COLUMNS			("columns"),
	UPDATES			("updates"),
	AVAILABLE		("available"),
	DEVICES			("devices"),
	OTMS_DATE		("otms_date"),
	AUTH_TOKEN		("auth_token"),
	FOREMAN_NAME	("foreman_name"),
	MESSAGES		("messages"), 
	FIRST_NAME 		("first_name"),
	LAST_NAME		("last_name")
	;

	final String name;

	API_PACKET_DATA(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return this.name;
	}
    };

    // Keys in response packets
    public enum API_PACKET_RESPONSE {
	PACKET_ID	("packet_id"),
	STATUS		("status"),
	DATA		("data"),
	MESSAGES	("messages"),
	ERROR		("error")
	;

	public final String name;

	API_PACKET_RESPONSE(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return this.name;
	}
    };

    public enum API_STATUS {
	ERROR (0),
	SUCCESS (1),
	;

	public final int status;

	API_STATUS(int status) {
	    this.status = status;
	}

	@Override
	public String toString() {
	    return Integer.toString(this.status);
	}
    }

    public enum API_PACKET_ERROR {
	NO_POST		("no POST"),
	NO_PACKET	("no packet"),
	BAD_PACKET	("bad packet"),
	BAD_TYPE	("bad packet type"),
	BAD_FORMAT	("bad packet format"),
	AUTH_DENIED	("bad authentication"),
	EXCEPTION	("exception")
	;

	public final String name;

	API_PACKET_ERROR(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return this.name;
	}
    };

}
