package com.reliance.api.requests;

import android.os.AsyncTask;

import com.reliance.api.CustomHttpClient;
import com.reliance.api.IApiResponseHandler;
import com.reliance.api.ServerResponse;

public class ApiRequestTask extends AsyncTask<IServerRequest, Integer, ServerResponse>{

	IApiResponseHandler handler;
	
	public ApiRequestTask(IApiResponseHandler handler) {
		this.handler = handler;
	}
	
	@Override
	protected void onPreExecute() {
		handler.onPreExecute();
	}
	@Override
	protected ServerResponse doInBackground(IServerRequest... params) {
		IServerRequest request = params[0];
		return CustomHttpClient.postEntity(request);
	}
	
	@Override
	protected void onPostExecute(ServerResponse result) {
		handler.onResponse(result);
	}

}
