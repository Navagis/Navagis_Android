package com.reliance.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
/**
 * The location handler handles the GPS radio and provides GPS coordinates
 * when asked for. The location handler will turn the radio on only when needed
 * to save battery life
 */
public class LocationHandler implements LocationListener {
	private static final int MINIMUM_DISTANCE = 500;
	private static final int MINIMUM_TIME = 1*60*1000;

	private static final long BEST_TIME_THRESHOLD = 2*60*1000;
	private static final float BEST_ACCURACY_THRESHOLD = 200;
	private LocationManager locationManager;
	private Location location;	
	private Runnable onChangeRunnable;


	public LocationHandler(Context context, Runnable onChangeRunnable) {
		this.onChangeRunnable = onChangeRunnable;
		if (locationManager == null)
			locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean isGpsTurnedOn() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public void start(){		
		try {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					LocationHandler.MINIMUM_TIME, 
					LocationHandler.MINIMUM_DISTANCE,
					this
					);
		} catch (IllegalArgumentException e) {
			// No network provider
		}

		try {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					LocationHandler.MINIMUM_TIME, 
					LocationHandler.MINIMUM_DISTANCE,
					this
					);
		} catch (IllegalArgumentException e) {
			// No GPS provider
		}
	}

	public void stop(){
		if (locationManager != null)
			locationManager.removeUpdates(this);		
	}

	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

	public Location getLocation() {
		return location;
	}

	public Double getLatitude(){		
		if (location == null)
			return null;

		try {
			return location.getLatitude();
		} catch(Exception e) {
			return null;
		}
	}

	public Double getLongitude(){
		if (location == null)
			return null;

		try {
			return location.getLongitude();
		} catch(Exception e) {
			return null;
		}
	}

	public Double getAltitude(){
		if (location == null)
			return null;

		try {
			return location.getAltitude();
		} catch(Exception e) {
			return null;
		}
	}

	public Float getAccuracy(){
		if (location == null)
			return null;

		try {
			return location.getAccuracy();
		} catch(Exception e) {
			return null;
		}
	}

	public Long getTimestamp(){
		if (location == null)
			return null;

		return location.getTime();
	}

	public Float getSpeed(){
		if (location == null)
			return null;

		try {
			return location.getSpeed();
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public void onLocationChanged(Location newloc) {
		if (!isBetterLocation(newloc, location))
			return;

		location = newloc;
		if (this.onChangeRunnable != null)
			onChangeRunnable.run();
	} 

	@Override
	public void onProviderDisabled(String provider) {
		Util.logD(provider + " disabled.");
	} 

	@Override
	public void onProviderEnabled(String provider) {
		Util.logD(provider + "enabled.");
	} 

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Util.logD(provider + " status changed.");
	}

	// Copied from http://developer.android.com/guide/topics/location/strategies.html
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > BEST_TIME_THRESHOLD;
		boolean isSignificantlyOlder = timeDelta < -BEST_TIME_THRESHOLD;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > BEST_ACCURACY_THRESHOLD;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}


}


