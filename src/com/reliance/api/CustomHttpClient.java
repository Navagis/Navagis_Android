package com.reliance.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.reliance.api.requests.IServerRequest;
import com.reliance.utils.Util;

public class CustomHttpClient {
	private static final String API_URL = "http://evm.navagis.com/RelianceCase3/index.php/";
	
	// time out 
	private static final int MILLIS_PER_SEC		= 1000;
	private static final int TIMEOUT_SOCKET		= 10 * 60 * MILLIS_PER_SEC;
	private static final int TIMEOUT_CONNECTION	= 10 * 60 * MILLIS_PER_SEC;


	/**
	 * Returns an instance of HttpClient with a certificate and credentials to access IMCS server.
	 * @return HttpClient
	 */
	public static HttpClient getNewHttpClient() {
		try {
			// set scope and user credentials
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			HttpParams httpParameters = new BasicHttpParams();

			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);

			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);

			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 8080));
			registry.register(new Scheme("https", sf, 8888));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParameters, registry);

			DefaultHttpClient httpClient = new DefaultHttpClient(ccm, httpParameters);
			httpClient.setCredentialsProvider(credsProvider);

			return httpClient;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
	
	public static ServerResponse postEntity(IServerRequest request) {
		if(request == null) throw new NullPointerException("Json request required");
		
		JSONObject jsResponse = null;
		try {
			StringEntity entity = new StringEntity(request.getJsonRequest().toString());
			entity.setContentType("application/json");
			
			String serverUrl = API_URL+request.getRequestType().toString();
			Util.logD(serverUrl+" Server request :: "+request.getJsonRequest().toString());

			HttpPost postRequest = new HttpPost(serverUrl);
			postRequest.setEntity(entity);
			postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Accept-Encoding", "gzip");
			
			HttpResponse response = getNewHttpClient().execute(postRequest);
			jsResponse = getJSONFromResponse(response);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Util.logE(e.getMessage());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Util.logE(e.getMessage());

		} catch (IOException e) {
			e.printStackTrace();
			Util.logE(e.getMessage());
		} 
		
		return ServerResponseFactory.getResponse(jsResponse);
	}

	private static JSONObject getJSONFromResponse(HttpResponse response) {
		if (response == null) return null;
		
		HttpEntity entity = response.getEntity();

		if (entity == null) return null;

		try {
			InputStream instream = entity.getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
			}
			String resultString = IOUtils.toString(instream);
			Util.logE("RESULTSTRING "+resultString);
			return new JSONObject(resultString);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
