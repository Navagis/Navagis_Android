package com.navagis.utils;

import com.navagis.api.ServerResponse;

public interface ServerResultHandler {
	public void onResult(ServerResponse response);
}
