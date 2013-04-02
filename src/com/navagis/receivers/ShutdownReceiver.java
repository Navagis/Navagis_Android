package com.navagis.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.navagis.api.ServerResponse;
import com.navagis.constants.ALERT;
import com.navagis.constants.DATETIME_FORMAT;
import com.navagis.main.RelianceApplication;
import com.navagis.models.Alert;
import com.navagis.utils.LocationHandler;
import com.navagis.utils.ServerResultHandler;
import com.navagis.utils.Util;

public class ShutdownReceiver extends BroadcastReceiver {
	private LocationHandler locationHandler;
	
	public ShutdownReceiver() {
		locationHandler = RelianceApplication.getLocationHandler();
	}

    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	if(!locationHandler.isGpsTurnedOn()) {
			this.abortBroadcast();
		}
    	
    	int assetId = RelianceApplication.getAssetId();
    	
    	// send alert
		Alert alert = new Alert();
		alert.setMessage("Device: "+RelianceApplication.getDeviceId()+" with Asset Id:"+assetId+" is shutting down");
		alert.setTitle(ALERT.TITLE_DEVICE_OFF.toString());
		alert.setAssetId(assetId);
		alert.setEmail(ALERT.EMAIL.toString());
		alert.setCreateTime(Util.getCurrentDateTime(DATETIME_FORMAT.SERVER));
		
		double lat = (locationHandler.getLatitude() == null) ? 0 : locationHandler.getLatitude();
		double lng = (locationHandler.getLongitude() == null) ? 0 : locationHandler.getLongitude();
		
		alert.setLat(lat);
		alert.setLng(lng);
		
		Util.generateAlert(alert, new ServerResultHandler() {
			
			@Override
			public void onResult(ServerResponse response) {	
				if(response.isSuccess())
					Util.logD("Alert(Device off) sent");
				else
					Util.logE("Alert(Device off) not sent");
			}
		});
		
    	
    }
    
    
}
