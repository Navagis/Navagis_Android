package com.reliance.constants;

public enum DATETIME_FORMAT {
	DASH 			("MM-dd-yyyy hh:mm:ss"),
	SLASH 			("MM/dd/yyyy hh:mm:ss"),
	ASSET 			("yyyy-MM-dd HH:mm:ss.S"),
	DATE_ONLY 		("MM/dd/yyyy"),
	TIME_ONLY 		("hh:mm:ss"), 
	SERVER			("yyyy-MM-dd HH:mm:ss"),
	READABLE		("E, MMM d, yyyy HH:mm:ss") 
	;
	
	final String name;
	DATETIME_FORMAT (String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
