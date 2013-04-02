package com.reliance.api.requests;

import org.json.JSONObject;


public class AlertRequest implements IServerRequest {

	JSONObject js;
	
	@Override
	public REQUEST getRequestType() {
		return REQUEST.ALERT;
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
