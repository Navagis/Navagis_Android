package com.reliance.api;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerResponse {
	JSONObject json;
	boolean isSuccess;
	String errors;
	
	public enum RESPONSE {
		SUCCESS ("success"),
		ERRORS ("errors"),
		;
		
		final String arg;
		RESPONSE(String arg) {
			this.arg = arg;
		}
		
		public String toString() {
			return this.arg;
		}
	}
	
public  ServerResponse(JSONObject json) {
		this.json = json;
		this.parseJson();
}

public JSONObject getJSONResponse() {
	return this.json;
}
public boolean isSuccess() {
	return isSuccess;
}

public String getErrorMessage() {
	if(this.errors == null)
		return "";
	
	return this.errors;
}

protected void parseJson() {	
	try {
		this.isSuccess = json.getBoolean(RESPONSE.SUCCESS.toString());
		
		if(!isSuccess)
			this.errors = json.getString(RESPONSE.ERRORS.toString());

	} catch (JSONException e) {
		e.printStackTrace();
	}
	
}


}
