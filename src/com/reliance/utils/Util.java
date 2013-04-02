package com.reliance.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.reliance.api.CustomHttpClient;
import com.reliance.api.ServerResponse;
import com.reliance.api.requests.AlertRequest;
import com.reliance.api.requests.IServerRequest;
import com.reliance.constants.Constants;
import com.reliance.constants.DATETIME_FORMAT;
import com.reliance.constants.ICON;
import com.reliance.main.RelianceApplication;
import com.reliance.models.Alert;
import com.reliance.utils.LocationHandler;
import com.reliance.utils.MailService;
import com.reliance.utils.ServerResultHandler;

public final class Util {

	private static Util instance = new Util();
	private static LocationHandler location;
	
	private Util() {}
	
	/**
	 * This function returns only a single instance of Util class
	 * @return instance of Util 
	 */
	public static Util getIntance() {
		return instance;
	}
	
	
	/**
	 * Log debug message
	 * @param message
	 */
	public static void logD(String message) {
		if(message != null) {
			Log.d(RelianceApplication.TAG(), message);
		}
	}
	
	/**
	 * Log verbose message
	 * @param message
	 */
	public static void logV(String message) {
		if(message != null) {
			Log.v(RelianceApplication.TAG(), message);
		}
	}
	
	/**
	 * Log info message
	 * @param message
	 */
	public static void logI(String message) {
		if(message != null) {
			Log.i(RelianceApplication.TAG(), message);
		}
	}
	
	/**
	 * Log error message
	 * @param message
	 */
	public static void logE(String message) {
		if(message != null) {
			Log.e(RelianceApplication.TAG(), message);
		}
	}
	
	public static Intent viewOnMap(String address) {
	    try {
			return new Intent(Intent.ACTION_VIEW,
			                  Uri.parse(String.format("geo:0,0?q=%s",
			                                          URLEncoder.encode(address, "UTF-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    
	    return null;
	}

	public static Intent viewOnMap(String lat, String lng) {
	    return new Intent(Intent.ACTION_VIEW,
	                      Uri.parse(String.format("geo:%s,%s", lat, lng)));
	}
	
	/**
	 * Merges update object to origin. Will ignore null values
	 * @param origin
	 * @param update
	 */
	public static void merge(final Object origin, final Object update){
	    if(!origin.getClass().isAssignableFrom(update.getClass())){
	        return;
	    }

	    Method[] methods = origin.getClass().getMethods();

	    for(Method fromMethod: methods){
	        if(fromMethod.getDeclaringClass().equals(origin.getClass())
	                && fromMethod.getName().startsWith("get")){

	            String fromName = fromMethod.getName();
	            String toName = fromName.replace("get", "set");

	            try {
	                Method toMethod = origin.getClass().getMethod(toName, fromMethod.getReturnType());
	                Object value = fromMethod.invoke(update, (Object[])null);
	                if(value != null){
	                    toMethod.invoke(origin, value);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            } 
	        }
	    }
	}
	
	/**
	 * Convert a millisecond duration to a string format
	 * 
	 * @param millis a duration to convert to a string form
	 * @return A string of the form "X Hours Y Minutes Z Seconds".
	 * @throws Exception
	 */
	public static String getDurationBreakdown(long millis){
		/*if (millis == 0) {
			throw new IllegalArgumentException(
					"Duration must be greater than zero!");
		}*/

		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder();
		sb.append(pad(hours));
		sb.append(":");
		sb.append(pad(minutes));
		sb.append(":");
		sb.append(pad(seconds));

		return (sb.toString());
	}

	public static String pad(long c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	/**
	 * Returns a random latitude coordinate
	 * 
	 * @return double coordinate
	 */
	public static double getRandomLatitude() {
		double minLat = -90.00;
		double maxLat = 90.00;      
		double latitude = minLat + (Math.random() * ((maxLat - minLat) + 1));

		return latitude;
	}

	/**
	 * Returns a random longitude coordinate
	 * 
	 * @return double coordinate
	 */
	public static double getRandomLongitude() {
		double minLon = 0.00;
		double maxLon = 180.00;     
		double longitude = minLon + (Math.random() * ((maxLon - minLon) + 1));

		return longitude;
	}
	
	/**
	 * Returns the current time with the provided format. Default format is mm/dd/yyyy hh:mm:ss
	 * 
	 * @param format 
	 * @return time in mm/dd/yyyy hh:mm:ss
	 * @throws Exception
	 */
	public static final String getCurrentDateTime(DATETIME_FORMAT format) {

		DateTimeFormatter dtf = DateTimeFormat.forPattern(format.toString()).withLocale(Locale.ENGLISH);
		DateTime dt = new DateTime(DateTimeZone.getDefault());
		String currentTime = dtf.print(dt);

		return currentTime;
	}
	
	public static void sendEmail(Alert alert) {
//		if(alert == null) throw new NullPointerException("Alert required");
		
/*		DriverDataSource ads = (DriverDataSource) DataSourceFactory.getRealDataSource(TABLE.ALERT);
		ads.open();
		Driver dr = ads.getDriver(alert.getDriverId());*/
		
		String user = "joe.buza@navagis.com";
		String pass = "mujuni8911";
		String from = "joe.buza@navagis.com";
		String subject = "Test Email Reliance";
		String body = "Test email reliance";
		
		MailService ms = new MailService(user, pass);
		ms.setFrom(from);
		ms.setSubject(subject);
		ms.setBody(body);
		ms.setTo(Constants.RECIPIENTS_EMAILS);
		try {
			ms.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void hideKeyboard(View v) {
		if(v == null) return;
		
		InputMethodManager imm = (InputMethodManager)RelianceApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	public static void sendLogin()	{
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.am.cma", new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Util.logD("RESPONSE "+response);
		    }
		    
		    @Override
		    public void onFailure(Throwable exception, String response) {
		    	exception.printStackTrace();
		        Util.logD("FAILURE "+response);
		    }
		});
	}
	
	/**
	 * Returns a resource id to the drawable that represents the icon. If icon is null, it will be defaulted to ICON.NONE which return 0---. 
	 * @param icon
	 * @return
	 */
	public static int  getIcon(ICON icon) {
		if(icon == null)
			icon = ICON.NONE;

		switch(icon) {
		case ALERT:
			return android.R.drawable.ic_dialog_alert;
		case WARNING:
		case DEFAULT:
			return android.R.drawable.ic_dialog_info;
		default:
			throw new IllegalArgumentException("Icon not found");
		}
	}
	
	/*
	public static void generateNotification(Context context, String message, Intent notificationIntent) {
		// TODO add a white icon
//		int icon = R.drawable.white_icon;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);

		if(notificationIntent == null) {
			notificationIntent = new Intent(context, MainActivity.class);
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		}

		// set intent so it does not start a new activity
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		String title = context.getString(R.string.app_name);

		Notification.Builder builder = new Notification.Builder(context);
		builder.setContentIntent(intent)
		.setWhen(when)
//		.setSmallIcon(icon)
		.setAutoCancel(true)
		.setContentTitle(title)
		.setContentText(message);		
		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		notificationManager.notify(0, notification);      

	} */
	
	
	public static void generateAlert(final Alert alert, final ServerResultHandler resultHandler) {
		//convert alert to json
		String json = RelianceApplication.getGson().toJson(alert);
		final AlertRequest request = new AlertRequest();
		try {
			request.setJsonRequest(new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		new AsyncTask<IServerRequest, Integer, ServerResponse>() {
			ServerResultHandler handler;
			protected void onPreExecute() {
				handler = resultHandler;
			};

			@Override
			protected ServerResponse doInBackground(IServerRequest... params) {
				IServerRequest request = params[0];
				return CustomHttpClient.postEntity(request);
			}
			
			@Override
			protected void onPostExecute(ServerResponse result) {
				handler.onResult(result);
			}
			
		}.execute(request);
	}
	
	/*
	public static void sendGpsAlert() {
		Context context = RelianceApplication.getContext();
        int icon = R.drawable.ic_launcher_white;

		
		Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    intent.setAction(Long.toString(System.currentTimeMillis()));
	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,  Intent.FLAG_ACTIVITY_NEW_TASK);

	    Notification.Builder builder = new Notification.Builder(context);
	    builder.setContentIntent(pendingIntent)
	    .setSmallIcon(icon)
	    .setWhen(System.currentTimeMillis())
	    .setAutoCancel(true)
	    .setContentTitle("GPS/Location Settings not enabled")
	    .setContentText("Tracking features disabled");

	    Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		
		// set icon
		notification.icon = icon;

	    NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    nManager.notify((int)System.currentTimeMillis(), notification);
	}
	*/
	
	/**
	 * Returns an array adapter
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> ArrayAdapter<T> getSpinnerArrayAdapter(Context context, List<T> list) {
		ArrayAdapter<T> spinnerArrayAdapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, list);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return spinnerArrayAdapter;
	}

}
