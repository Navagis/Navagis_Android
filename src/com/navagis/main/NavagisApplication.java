package com.navagis.main;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.google.gson.Gson;
import com.navagis.constants.Constants;
import com.navagis.constants.ICON;
import com.navagis.database.DatabaseManager;
import com.navagis.main.R;
import com.navagis.models.User;
import com.navagis.services.SyncerService;
import com.navagis.services.TrackerService;
import com.navagis.utils.LocationHandler;
import com.navagis.utils.Util;

public class NavagisApplication extends Application {
	private static Context context;
	private static LocationHandler locationHandler;
	private static SharedPreferences sharedPreferences;
	private static NavagisApplication instance = new NavagisApplication();
	private static Activity currentActivity;
	private static String currentDeviceId;
	private static Gson gson;

	// Application package name
	public static String PACKAGE_NAME;

	public static NavagisApplication getInstance() {
		return instance;
	}


	@Override
	public void onCreate() {
		super.onCreate();

		context = getApplicationContext();

		PACKAGE_NAME = context.getPackageName();

		currentDeviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		gson = new Gson();
		
		locationHandler = new LocationHandler(context, new Runnable() {
			@Override
			public void run() {}
		});
		
		locationHandler.start();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		stopSyncerService();
		stopTrackerService();
	}
	public static Context getContext() {
		return context;
	}

	public static SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}

	public static Activity getCurrentActivity() {
		return currentActivity;
	}

	public static void setCurrentActivity(Activity currentActivity) {
		NavagisApplication.currentActivity = currentActivity;
	}

	public static String getCurrentDeviceId() {
		return currentDeviceId;
	}

	public static void startActivity(Class<? extends BaseActivity> activity) {
		startActivity(activity, null);
	}

	public static void startActivity(Class<? extends BaseActivity> activityClass, Bundle extras) {
		if (currentActivity == null)
			return;

		Intent intent = new Intent(currentActivity, activityClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		currentActivity.startActivity(intent);

	}

	public static String TAG() {
		return context.getString(R.string.app_name);
	}

	/**
	 * Returns device id
	 * 
	 * @param context
	 * @return String device id
	 */
	public static final String getDeviceId() {
		String android_id = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		return android_id;
	}
	
	public static Gson getGson() {
		return gson;
	}

	private static String versionName = null;
	public static String getVersionName() {
		if (versionName != null)
			return versionName;

		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionName;
	}

	public static void showToast(int resId) {
		if (currentActivity != null)
			showToast(currentActivity.getText(resId).toString());
	}

	public static void showToast(final String text) {
		if (currentActivity != null) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					Toast.makeText(currentActivity, text, Toast.LENGTH_LONG).show();
				}
			};
			currentActivity.runOnUiThread(r);
		}
	}

	public static void setActivityTime() {
		SharedPreferences.Editor spe = NavagisApplication.sharedPreferences.edit();
		spe.putLong(Constants.LAST_ACTIVITY_TIME, new Date().getTime());
		spe.commit();

	}
	
	public static LocationHandler getLocationHandler() {
		return locationHandler;
	}

	public static long retrieveLastActivityTime() {		
		return sharedPreferences.getLong(Constants.LAST_ACTIVITY_TIME, 0);
	}

	public static void clearLastActivityTime() {
		SharedPreferences.Editor spe = NavagisApplication.sharedPreferences.edit();
		spe.remove(Constants.LAST_ACTIVITY_TIME);
		spe.commit();
	}

	public static void registerUser(final User user) {
		SharedPreferences.Editor e = NavagisApplication.getSharedPreferences().edit();
		e.putString(Constants.KEY_USER_EMAIL, user.getEmail());
		e.putString(Constants.KEY_USER_PASSWORD, user.getPassword());
		e.commit();
	}
	public static User getUser() {
		User user = new User();
		user.setEmail(sharedPreferences.getString(Constants.KEY_USER_EMAIL, ""));
		user.setPassword(sharedPreferences.getString(Constants.KEY_USER_PASSWORD, ""));
		return user;
	}

	public static void saveAsset(int assetId, String assetName) {
		SharedPreferences.Editor e = getSharedPreferences().edit();
		e.putInt(Constants.KEY_ASSET_ID, assetId);
		e.putString(Constants.KEY_ASSET_NAME, assetName);
		e.commit();
	}

	public static void startTrackerService() {
		Intent service = new Intent(context, TrackerService.class);
		context.startService(service);
	}

	public static void startTrackerService(Long outageId, String outageStatus) {
		Intent service = new Intent(context, TrackerService.class);
		service.putExtra(Constants.KEY_ASSET_ID, outageId);
		context.startService(service);
	}

	public static void startSyncerService() {
		Intent service = new Intent(context, SyncerService.class);
		context.startService(service);
	}

	protected static void stopTrackerService() {
		Intent service = new Intent(context, TrackerService.class);
		context.stopService(service);
	}

	protected static void stopSyncerService() {
		Intent service = new Intent(context, SyncerService.class);
		context.stopService(service);
	}

	public static void showErrorDialog(String error) {
		showAlertDialog("Error", error);
	}

	public static void showErrorDialog(int resId) {
		showAlertDialog("Error", context.getString(resId));
	}

	public static void showErrorDialog(final List<String> errors) {
		if(errors == null || errors.isEmpty()) return;

		StringBuilder b = new StringBuilder();
		boolean isFirst = true;
		for (String s : errors) {
			if (isFirst) {
				isFirst = false;
			} else {
				b.append('\n');
			}
			b.append(s);
		}

		if(b.toString() != "")
			showAlertDialog("Error:", b.toString());
	}

	public static void showAlertDialog(String title, String errors) {
		if(!isApplicationRunning()) return;

		AlertDialog.Builder alert = new AlertDialog.Builder(currentActivity);
		alert.setTitle(title)
		.setMessage(errors)
		.setIcon(Util.getIcon(ICON.ALERT))
		.setNegativeButton(android.R.string.ok, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		}).create().show();
	}

	public static boolean isApplicationRunning() {
		if(currentActivity == null)
			return false;

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// get the info from the currently running task
		List < ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
		if (!taskInfo.isEmpty()) {
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			//if  app is running
			if (!componentInfo.getPackageName().equalsIgnoreCase(
					PACKAGE_NAME))
				return false;
		}

		return true;
	}

	public static void startBackgroundServices() {
		showToast("Device is tracking");
		startTrackerService();
		startSyncerService();
	}
	
	public static void stopBackgroundServices() {
		showToast("Device stopped tracking");
		stopSyncerService();
		stopTrackerService();
	}
	
	/**
	 * Creates an alert before loggin out
	 */
	public static void promptLogOutAlert(){
		AlertDialog.Builder alert = new AlertDialog.Builder(currentActivity);
		alert.setTitle("Sign Out")
		.setMessage("Are you sure you wish to signout?")
		.setIcon(Util.getIcon(ICON.ALERT))
		.setNegativeButton(android.R.string.cancel, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setPositiveButton(android.R.string.ok, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				NavagisApplication.logOut();
			}
		}).create().show();
	}
	
	public static void logOut() {
		stopBackgroundServices();
		DatabaseManager.emptyAllTables();
		startActivity(LoginActivity.class);
	}

	private static ProgressDialog progressDialog = null;
	public static boolean showProgressDialog(String title, String message) {
		if(!isApplicationRunning()) return false;

		if (progressDialog != null) return false;
		progressDialog = ProgressDialog.show(currentActivity, title, message, true);
		return true;
	}

	public static boolean showProgressDialog(String message) {
		return showProgressDialog(null, message);
	}

	public static boolean dismissProgressDialog() {
		if (progressDialog == null) return false;
		progressDialog.dismiss();
		progressDialog = null;
		return true;
	}

	/**
	 * Checks for network availability
	 * 
	 * @return boolean
	 */
	public static boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	public static int getAssetId() {
		SharedPreferences sp = getSharedPreferences();
		return sp.getInt(Constants.KEY_ASSET_ID, -1);
	}
	
	public static String getAppVersion() {
		String myVersion = android.os.Build.VERSION.RELEASE;
		return myVersion;
	}
	
	public static int getOsVersion() {
		int sdkVersion = android.os.Build.VERSION.SDK_INT;
		return sdkVersion;
	}
	
	public static String getDeviceBrand() {
		String brand = android.os.Build.BRAND;
		return brand;
	}

}
