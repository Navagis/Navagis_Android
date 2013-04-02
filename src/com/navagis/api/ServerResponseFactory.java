package com.navagis.api;

import org.json.JSONObject;

public class ServerResponseFactory {
	public static ServerResponse getResponse(JSONObject js) {
		if(js != null)
			return new ServerResponse(js);
		else
			return new NullResponse(js);
	}
}
