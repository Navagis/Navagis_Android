package com.reliance.api.requests;

import org.json.JSONObject;


public class WaypointsRequest implements IServerRequest {
	JSONObject js;

	@Override
	public REQUEST getRequestType() {
		return REQUEST.WAYPOINTS;
	}

	@Override
	public JSONObject getJsonRequest() {
		return js;
	}

	@Override
	public void setJsonRequest(JSONObject request) {
		this.js = request;

	}


}
