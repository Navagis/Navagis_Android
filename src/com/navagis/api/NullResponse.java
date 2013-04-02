package com.navagis.api;

import org.json.JSONObject;

public class NullResponse extends ServerResponse {

	private static NullResponse instance;
	
	public static NullResponse getInstance(JSONObject json) {
		
		if(instance == null)
			instance = new NullResponse(json);
		
		return instance;
	}
	public NullResponse(JSONObject json) {
		super(json);
	}
	
	@Override
	public String getErrorMessage() {
		return "Unable to connect to the server";
	}
	
	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	protected void parseJson() {
		return;
	}
}
