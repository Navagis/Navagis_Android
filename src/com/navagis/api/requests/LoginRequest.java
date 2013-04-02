package com.navagis.api.requests;

import org.json.JSONObject;


public class LoginRequest implements IServerRequest {
	JSONObject js;

	@Override
	public REQUEST getRequestType() {
		return REQUEST.ASSET;
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
