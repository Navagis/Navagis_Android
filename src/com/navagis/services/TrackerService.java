package com.navagis.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.navagis.api.ServerResponse;
import com.navagis.constants.ALERT;
import com.navagis.constants.DATETIME_FORMAT;
import com.navagis.dao.abstracts.DataSourceFactory;
import com.navagis.daos.real.AssetTrackingDataSource;
import com.navagis.database.DatabaseAssets.TABLE;
import com.navagis.main.RelianceApplication;
import com.navagis.models.Alert;
import com.navagis.models.AssetTracking;
import com.navagis.utils.LocationHandler;
import com.navagis.utils.ServerResultHandler;
import com.navagis.utils.Util;

public class TrackerService extends Service {
	private static final int TIME_INTERVAL_SEC = 2*60*1000; // 2 minutes
	private static Location prevLocation;
	private LocationHandler locationHandler;
	String crewCode;
	String foremanNumber;
	private Integer assetId;
	private Timer timer;

	@Override
	public void onCreate() {
		super.onCreate();
			
		Util.logD("TrackerService started");
	
		// Start location manager
		locationHandler = RelianceApplication.getLocationHandler();
		
		if(!locationHandler.isGpsTurnedOn()) {
		    this.stopSelf();
		}
		
		// Check if GPS is enabled 
		assetId = RelianceApplication.getAssetId();
		
		// Start timer to start tracking user location
		TimerTask timerTask = buildTask();
		timer = new Timer("TrackTimer");
		timer.schedule(timerTask, 1, TIME_INTERVAL_SEC);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}


	
	@Override
	public void onDestroy() {
		locationHandler.stop();
		if (timer != null) {
			timer.cancel();
		}
		
		super.onDestroy();
	}

	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private TimerTask buildTask() {
		return new TimerTask() {
			@Override
			public void run() {				
				if(!isValidLocation())
					return;
								
				if(!hasLocationChanged(locationHandler.getLocation()))
					sendAlert();
				
				String curTime = Util.getCurrentDateTime(DATETIME_FORMAT.SERVER);
				Util.logD(curTime);
				
				// Save tracks
				AssetTracking at = new AssetTracking();
				at.setAssetId(assetId);
				at.setTrackingDateTime(curTime);
				at.setLatitude(locationHandler.getLatitude());
				at.setLongitude(locationHandler.getLongitude());
				
				AssetTrackingDataSource ads = (AssetTrackingDataSource) DataSourceFactory.getRealDataSource(TABLE.ASSET_TRACKING);
				ads.open();
				ads.insertRow(at);
				
			}
		};
	}

	/**
	 * @return
	 */
	private boolean isValidLocation() {
		if(locationHandler.getLatitude() == null || locationHandler.getLongitude() == null)
			return false;
		
		return (locationHandler.getLatitude() != (double)0) || (locationHandler.getLongitude() != (double)0);
	}
	
	private boolean hasLocationChanged(Location newLoc) {
		boolean retValue = true;
		
		if(prevLocation == null) {
			prevLocation = newLoc;
			return retValue;
		}
		
		double oldLat = roundDouble(prevLocation.getLatitude());
		double oldLng = roundDouble(prevLocation.getLongitude());

		double newLat = roundDouble(newLoc.getLatitude());
		double newLng = roundDouble(newLoc.getLongitude());

		prevLocation = newLoc;
		
		if((oldLat == newLat) && (oldLng == newLng)){
			retValue = false;
		}

		return retValue;
	}
	
	private double roundDouble(double value) {
		double finalValue = Math.round( value * 100.0 ) / 100.0;
		return finalValue;
	}
	
	private void sendAlert() {
		// send alert
		int assetId = RelianceApplication.getAssetId();
		
		Alert alert = new Alert();
		alert.setMessage("Asset: "+assetId+" is not moving");
		alert.setTitle(ALERT.TITLE_NO_MOVEMENT.toString());
		alert.setAssetId(assetId);
		alert.setEmail(ALERT.EMAIL.toString());
		alert.setCreateTime(Util.getCurrentDateTime(DATETIME_FORMAT.SERVER));
		alert.setLat(locationHandler.getLatitude());
		alert.setLng(locationHandler.getLongitude());

		Util.generateAlert(alert, new ServerResultHandler() {

			@Override
			public void onResult(ServerResponse response) {
				if(response.isSuccess())
					Util.logD("No movement alert success");
				else
					Util.logE("No movement alert failed");

			}
		});
	}

}
