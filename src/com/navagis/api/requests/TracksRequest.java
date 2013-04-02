package com.navagis.api.requests;

import org.json.JSONObject;


public class TracksRequest implements IServerRequest {

	JSONObject js;
	private static int instanceCounter = 0;

	public TracksRequest() {
		instanceCounter++;
	}
	@Override
	public REQUEST getRequestType() {
		return REQUEST.TRACKS;
	}

	@Override
	public JSONObject getJsonRequest() {
		return js;
	}

	@Override
	public void setJsonRequest(JSONObject request) {
		this.js = request;
	}
	
	public int getId() {
		return instanceCounter;
	}

}
