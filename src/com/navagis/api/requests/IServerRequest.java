package com.navagis.api.requests;

import org.json.JSONObject;

public interface IServerRequest {
public enum REQUEST {
	ALERT ("alert/create"),
	ASSET ("asset/validate"),
	TRACKS ("tracks/create"),
	DROPS  ("materialDrops/create"),
	WAYPOINTS ("route/waypoints")
	;
	
	final String req;
	REQUEST(String req) {
		this.req = req;
	}
	
	public String toString() {
		return this.req;
	}
}

public REQUEST getRequestType();
public JSONObject getJsonRequest();
public void setJsonRequest(JSONObject request);
}
