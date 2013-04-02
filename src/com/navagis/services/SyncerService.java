package com.navagis.services;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.navagis.api.CustomHttpClient;
import com.navagis.api.ServerResponse;
import com.navagis.api.requests.TracksRequest;
import com.navagis.daos.real.AssetTrackingDataSource;
import com.navagis.main.NavagisApplication;
import com.navagis.models.AssetTracking;
import com.navagis.utils.Util;

public class SyncerService extends Service {
	private static final long MILLIS_PER_MINUTE = 60 * 1000;

	private static final long INITIAL_DELAY = 0;
	private static final long TASK_PERIOD = 1 * MILLIS_PER_MINUTE;

	private Timer timer;
	private static Gson gson;
	private static boolean isSyncing;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Util.logD("SyncerService started");

		// converts an object to json string
		gson = new Gson();

		isSyncing = false;
		initializeTimer();
	}
	
	public void initializeTimer() {
		if (!isSyncing) {
			if (timer != null) {
				timer.cancel();
				timer.purge();
			}
			TimerTask syncTask = syncTask();
			timer = new Timer("SyncerTimer");
			timer.schedule(syncTask, INITIAL_DELAY, TASK_PERIOD);
		}
	}
	
	/**
	 * Timer that calls tracking based on the interval its given 
	 * @return A timer task
	 */
	private TimerTask syncTask(){
		// this code gets triggered based on the interval 
		return new TimerTask() {
			@Override
			public void run() {
				TracksRequest message = null;
				try {
					isSyncing = true;
					if(!NavagisApplication.isNetworkAvailable())
						return;

					message = new TracksRequest();

					if (!beginPendingUpdate(message)) return;

					final ServerResponse response = CustomHttpClient.postEntity(message);
					if ((response == null) || !response.isSuccess()) {
						abortPendingUpdate(message);
					} else {
						commitPendingUpdate(message);
					}
					
				} catch(Exception e) {
					e.printStackTrace();
					abortPendingUpdate(message);
				} finally {
					isSyncing = false;

				}

			}
		};
	}


	protected static void commitPendingUpdate(TracksRequest message) {
		if(message == null) return;
		
		// delete pending tracking records
		AssetTrackingDataSource atd = new AssetTrackingDataSource();
		atd.open();
		atd.deleteAssetTracksWithPacketId(message.getId());
	}

	protected static void abortPendingUpdate(TracksRequest message) {
		if(message == null) return;
		
		// reset pending tracking records
		AssetTrackingDataSource atd = new AssetTrackingDataSource();
		atd.open();
		atd.resetPacketId(message.getId());
	}

	protected static boolean beginPendingUpdate(TracksRequest message) {
		// set asset tracking
		AssetTrackingDataSource atd = new AssetTrackingDataSource();
		atd.open();
		
		// init packet id
		atd.insertPacketId(message.getId());	
		
		// init 
		List<AssetTracking> assetTrackings = atd.getAllAssetTrackings();
		Type type = new TypeToken<List<AssetTracking>>() {}.getType();
		String s_asseTracks = gson.toJson(assetTrackings, type);
		JSONObject js = new JSONObject();
		try {
			JSONArray jsArray = new JSONArray(s_asseTracks);
			js.put("tracks", jsArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		message.setJsonRequest(js);	
		return ((assetTrackings.size() > 0));
	}


}
