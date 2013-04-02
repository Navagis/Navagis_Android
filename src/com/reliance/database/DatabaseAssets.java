package com.reliance.database;


public final class DatabaseAssets {

	static final String COMMA = ", ";

	public enum COLUMN {

		// Device Columns
		DEVICE_BRAND						("device_brand",	"VARCHAR(50)",	"",	""),
		DEVICE_ID							("device_id",	"VARCHAR(20)",	"PRIMARY KEY",	""),
		OS_VERSION							("os_version", "VARCHAR(10)", "", ""),
		STATUS_DATE_TIME					("status_date_time",	"TEXT",	"",	""),
		STATUS								("status",	"VARCHAR(20)",	"",	""),
		LAST_SYNC_DATE_TIME					("last_sync_date_time",	"TEXT",	"",	""),
		APPLICATION_VERSION					("application_version", "VARCHAR(10)","",""),

		// Asset_Tracking Columns
		TRACKING_DATE_TIME					("tracking_date_time",	"VARCHAR(50)",	"",	""),
		LATITUDE							("latitude",	"REAL",	"", ""),
		LONGITUDE							("longitude",	"REAL",	"",	""),
		ASSET_TRACKING_ID					("asset_tracking_id", "INTEGER ", "PRIMARY KEY AUTOINCREMENT", ""),
		PACKET_ID							("packet_id", "INTEGER", "", "NULL"),

		// User Columns
		FIRST_NAME							("first_name", "VARCHAR(50)", "",""),
		LAST_NAME							("last_name", "VARCHAR(50)", "",""),
		USER_EMAIL							("user_email","VARCHAR(50)", "PRIMARY KEY", ""),
		USER_ID								("user_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT", ""),
		PASSWORD							("password", "VARCHAR(50)", "", ""),
		
		// Material Drops
		DROP_NAME							("drop_name", "VARCHAR(50)", "",""),
		DROP_ID								("drop_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT", ""),
		DROP_CREATE_TIME					("drop_create_time", "VARCHAR(50)", "", ""),
		DROP_EXTRA							("drop_extra", "TEXT", "", ""),
		STORE								("store", "VARCHAR(100)", "", ""),						


		// Asset Columns
		ASSET_ID							("asset_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT", ""),
		ASSET_TYPE							("asset_type",	"VARCHAR(50)",	"",	""),
		ASSET_NAME							("asset_name",	"VARCHAR(50)",	"PRIMARY KEY",	""),
		CHECK_IN_DATE_TIME					("check_in_date_time", "TEXT", "", ""),
		CHECK_OUT_DATE_TIME					("check_out_date_time",	"TEXT",	"",	""),

		// Alert Columns
		ALERT_ID							("alert_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT",""),
		ROUTE_ID							("route_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT",""),
		MESSAGE								("message", "TEXT", "", ""),
		TITLE								("title", "VARCHAR(50)", "", ""),
		ALERT_EMAIL							("alert_email", "TEXT", "",""),
		ALERT_DATE_TIME						("alert_date_time", "TEXT", "",""), 
		;

		public final String name, type, def_modifier, ref_modifier;

		COLUMN(String name, String type, String def_modifier, String ref_modifier) {
			this.name = name;
			this.type = type;
			this.def_modifier = def_modifier;
			this.ref_modifier = ref_modifier;
		}

		public String getDef() {
			return this.name + " " + this.type + " " + this.def_modifier;
		}

		public String getRef() {
			return this.name + " " + this.type + " " + this.ref_modifier;
		}

		@Override
		public String toString() {
			return this.name;
		}

	}

	public enum TABLE {
		DEVICE 			("Device"),
		ASSET_TRACKING	("Asset_Tracking"),
		USER 			("User"),
		ASSET 			("Asset"),
		ALERT 			("Alert"),
		MATERIAL_DROPS			("Drops")
		;

		public final String name;
		TABLE(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}


	static final String CREATE_ASSET = "CREATE TABLE "+TABLE.ASSET+" ("
			+ COLUMN.ASSET_ID.getDef()+COMMA 
			+ COLUMN.ASSET_TYPE.getRef()+COMMA
			+ COLUMN.ASSET_NAME.getRef()+COMMA
			+ COLUMN.CHECK_IN_DATE_TIME.getRef()+COMMA
			+ COLUMN.CHECK_OUT_DATE_TIME.getRef()+COMMA
			+ COLUMN.ROUTE_ID.getRef()+COMMA
			+ COLUMN.USER_ID.getRef()+COMMA
			+ COLUMN.DEVICE_ID.getRef()
			+ ");"; 

	static final String CREATE_DEVICE = "CREATE TABLE "+TABLE.DEVICE+"("
			+ COLUMN.DEVICE_ID.getDef()+COMMA 
			+ COLUMN.DEVICE_BRAND.getRef()+COMMA
			+ COLUMN.OS_VERSION.getRef()+COMMA
			+ COLUMN.STATUS_DATE_TIME.getRef()+COMMA
			+ COLUMN.ASSET_ID.getRef()+COMMA
			+ COLUMN.STATUS.getRef()
			+ ");";
	
	static final String CREATE_DROPS = "CREATE TABLE "+TABLE.MATERIAL_DROPS+"("
			+ COLUMN.DROP_ID.getDef()+COMMA 
			+ COLUMN.DROP_NAME.getRef()+COMMA
			+ COLUMN.DROP_EXTRA.getRef()+COMMA
			+ COLUMN.DROP_CREATE_TIME.getRef()+COMMA
			+ COLUMN.PACKET_ID.getRef()
			+ ");";

	static final String CREATE_ASSET_TRACKING = "CREATE TABLE "+TABLE.ASSET_TRACKING+"("
			+ COLUMN.ASSET_TRACKING_ID.getDef()+COMMA 
			+ COLUMN.LATITUDE.getRef()+COMMA
			+ COLUMN.LONGITUDE.getRef()+COMMA
			+ COLUMN.TRACKING_DATE_TIME.getRef()+COMMA
			+ COLUMN.ASSET_ID.getRef()+COMMA
			+ COLUMN.PACKET_ID.getRef()
			+ ");";
	static final String CREATE_ALERT = "CREATE TABLE "+TABLE.ALERT+"("
			+ COLUMN.ALERT_ID.getDef()+COMMA 
			+ COLUMN.MESSAGE.getRef()+COMMA
			+ COLUMN.TITLE.getRef()+COMMA
			+ COLUMN.ALERT_EMAIL.getRef()+COMMA
			+ COLUMN.ASSET_ID.getRef()+COMMA
			+ COLUMN.USER_EMAIL.getRef()+COMMA
			+ COLUMN.PACKET_ID.getRef()+COMMA
			+ COLUMN.DEVICE_ID.getRef()
			+ ");";
	static final String CREATE_USER = "CREATE TABLE "+TABLE.USER+"("
			+ COLUMN.FIRST_NAME.getDef()+COMMA 
			+ COLUMN.LAST_NAME.getRef()+COMMA
			+ COLUMN.USER_EMAIL.getRef()+COMMA
			+ COLUMN.PASSWORD.getRef()
			+ ");";
	
	/**********************************************************************
     * Delete Statements
     **********************************************************************/
    public static final String DELETE_ASSETS = "DELETE FROM "+TABLE.ASSET;
    public static final String DELETE_ASSET_TRACKINGS = "DELETE FROM "+TABLE.ASSET_TRACKING;
    public static final String DELETE_USERS = "DELETE FROM "+TABLE.USER;
    public static final String DELETE_ALERTS = "DELETE FROM "+TABLE.ALERT;
    public static final String DELETE_MATERIAL_DROPS = "DELETE FROM "+TABLE.MATERIAL_DROPS;
	
}
