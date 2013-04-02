package com.reliance.api.requests;

import org.json.JSONObject;

public class MaterialDropsRequest implements IServerRequest{

	JSONObject request;
	
	@Override
	public REQUEST getRequestType() {
		return REQUEST.DROPS;
	}

	@Override
	public JSONObject getJsonRequest() {
		return request;
	}

	@Override
	public void setJsonRequest(JSONObject request) {
		this.request = request;
	}

}
