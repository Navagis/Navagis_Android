package com.navagis.api.requests;

import android.os.AsyncTask;

import com.navagis.api.CustomHttpClient;
import com.navagis.api.IApiResponseHandler;
import com.navagis.api.ServerResponse;

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
