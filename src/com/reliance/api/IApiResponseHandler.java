package com.reliance.api;

public interface IApiResponseHandler {
	public void onPreExecute();
	public void onResponse(ServerResponse response);
}
