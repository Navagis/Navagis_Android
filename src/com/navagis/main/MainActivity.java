package com.navagis.main;


import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.navagis.api.IApiResponseHandler;
import com.navagis.api.ServerResponse;
import com.navagis.api.requests.ApiRequestTask;
import com.navagis.api.requests.IServerRequest;
import com.navagis.api.requests.MaterialDropsRequest;
import com.navagis.api.requests.WaypointsRequest;
import com.navagis.constants.DATETIME_FORMAT;
import com.navagis.constants.ICON;
import com.navagis.main.R;
import com.navagis.models.MaterialDrops;
import com.navagis.utils.LocationHandler;
import com.navagis.utils.Util;

public class MainActivity extends BaseActivity implements OnClickListener{
	Button checkInButton;
	EditText dropNameEt, extraInfoEt;
	Spinner storeSpinner;
	LocationHandler locationHandler;
	MaterialDrops materialDrops;
	ProgressDialog materialDropDialog, waypointsDialog;
	Gson gson;
	TextView labelStore;
	
	static final int MATERIALDROP_DIALOG_KEY = 0;
	static final int WAYPOINTS_DIALOG_KEY = 1;
	static final int LOCATION_SETTINGS = 2;

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		checkInButton = (Button) findViewById(R.id.checkInButton);
		checkInButton.setOnClickListener(this);
		
		dropNameEt = (EditText) findViewById(R.id.dropName);
		storeSpinner = (Spinner) findViewById(R.id.storeSpinner);
		extraInfoEt = (EditText) findViewById(R.id.extraInfo);
		labelStore = (TextView) findViewById(R.id.labelStore);
		
		materialDrops = new MaterialDrops();
		
		gson = new Gson();
		
		locationHandler = NavagisApplication.getLocationHandler();
		startWaypointsTask();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
    	if(!locationHandler.isGpsTurnedOn()) {
    		AlertDialog.Builder alert = new AlertDialog.Builder(this);
    		alert.setTitle("GPS Disabled")
    		.setMessage("Please enable your GPS inorder to start tracking")
    		.setIcon(Util.getIcon(ICON.ALERT))
    		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.cancel();
    				enableCheckInButton(false);
    				NavagisApplication.stopBackgroundServices();
    			}
    		})
    		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    	    		MainActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    			}
    		}).create().show();
    		
		} else {
	    	NavagisApplication.startBackgroundServices();
			enableCheckInButton(true);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    ProgressDialog dialog = new ProgressDialog(this);

	    switch (id) {
	    case MATERIALDROP_DIALOG_KEY:
	        ((ProgressDialog) dialog).setIndeterminate(true);
	        dialog.setCancelable(false);
	        ((ProgressDialog) dialog)
	                .setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        dialog.setTitle("Check In");
	        dialog.setMessage("Sending request...");
	        dialog.show();
	        break;
	    case WAYPOINTS_DIALOG_KEY:
	        ((ProgressDialog) dialog).setIndeterminate(true);
	        dialog.setCancelable(false);
	        ((ProgressDialog) dialog)
	                .setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        dialog.setTitle("Store");
	        dialog.setMessage("Retrieving store locations...");
	        dialog.show();
	        break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_sign_out:
			NavagisApplication.promptLogOutAlert();
			break;
		default:
			return false;
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		NavagisApplication.promptLogOutAlert();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.checkInButton:
			sendMaterialDrops();
			break;
			default:
				break;
				
		}
		
	}
	
	public void clearFields() {
		dropNameEt.setText("");
		extraInfoEt.setText("");
	}
	
	public boolean validateFields() {
		String errorMessage = "";
		if(dropNameEt == null || TextUtils.isEmpty(dropNameEt.getText())) {
			errorMessage += "Name required \n";
		} 
		
		if(storeSpinner == null || storeSpinner.getSelectedItem().toString() == ""){
			errorMessage += "Store name required \n";
		}
		
		String extraInfo;
		if(extraInfoEt == null || TextUtils.isEmpty(extraInfoEt.getText()))
			 extraInfo = null;
		else
			extraInfo = extraInfoEt.getText().toString();
		
		if(errorMessage != "") {
			NavagisApplication.showErrorDialog(errorMessage);
			return false;
		} else {
			String curTime = Util.getCurrentDateTime(DATETIME_FORMAT.SERVER);

			materialDrops.setName(dropNameEt.getText().toString());
			materialDrops.setExtraInfo((extraInfo != null) ? extraInfo : "");
			materialDrops.setStoreAddress(storeSpinner.getSelectedItem().toString());
			materialDrops.setLat(locationHandler.getLatitude());
			materialDrops.setLng(locationHandler.getLongitude());
			materialDrops.setAsset_id(NavagisApplication.getAssetId());
			materialDrops.setCreateTime(curTime);
			
			Util.logD(storeSpinner.getSelectedItem().toString());
			return true;
		}
	}
	
	public void sendMaterialDrops() {
		
		if(!validateFields())
			return;
		else if(!NavagisApplication.isNetworkAvailable()) {
			NavagisApplication.showErrorDialog("No network connection");
			return;
		}

		String json = gson.toJson(materialDrops);
		MaterialDropsRequest request = new MaterialDropsRequest();
		try {
			request.setJsonRequest(new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		startMaterialDropsTask(request);		
	}
	
	private void startWaypointsTask() {
		int assetId = NavagisApplication.getAssetId();
		JSONObject js = new JSONObject();
		try {
			js.put("asset_id", assetId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		WaypointsRequest wr = new WaypointsRequest();
		wr.setJsonRequest(js);
	
		new ApiRequestTask(new IApiResponseHandler() {
			@Override
			public void onResponse(ServerResponse result) {
				waypointsDialog.dismiss();
				
				if(result == null) {
					NavagisApplication.showErrorDialog("Unable to connect to the server"); 
					return;
					};
				
				if(result.isSuccess()){
					JSONObject res = result.getJSONResponse();
					Util.logD(res.toString());
										
					try {
						Type type = new TypeToken<List<String>>(){}.getType();
					    List<String> waypoints = gson.fromJson(res.getString("waypoints"), type);
					    
					    // create a spinner with this array
					    ArrayAdapter<String> spinnerArrayAdapter = Util.getSpinnerArrayAdapter(MainActivity.this, waypoints);
					    storeSpinner.setAdapter(spinnerArrayAdapter);
					    
					    enableStoreAddress(true);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					enableStoreAddress(false);
					NavagisApplication.showAlertDialog("No Stores Found", "Unable to find store locations for this asset");
				}
			}
			@Override
			public void onPreExecute() {
				waypointsDialog = (ProgressDialog)onCreateDialog(WAYPOINTS_DIALOG_KEY);
			}
		}).execute(wr);
		
	}
	
	private void startMaterialDropsTask(IServerRequest request) {
		new ApiRequestTask(new IApiResponseHandler() {
			
			@Override
			public void onResponse(ServerResponse result) {

				materialDropDialog.dismiss();
				
				if(result == null) {NavagisApplication.showErrorDialog("Unable to connect to the server"); return;};
				
				if(result.isSuccess()){
					clearFields();
					NavagisApplication.showToast("Success");										
				} else {
					NavagisApplication.showErrorDialog(result.getErrorMessage());
				}
			}
			
			@Override
			public void onPreExecute() {
				materialDropDialog = (ProgressDialog)onCreateDialog(MATERIALDROP_DIALOG_KEY);
			}
		}).execute(request);
		
	}
	
	private void enableCheckInButton(boolean isEnabled) {
		checkInButton.setEnabled(isEnabled);
	}
	
	private void enableStoreAddress(boolean isEnabled) {
		if(isEnabled) {
			labelStore.setVisibility(View.VISIBLE);
			storeSpinner.setVisibility(View.VISIBLE);
		} else {
			labelStore.setVisibility(View.GONE);
			storeSpinner.setVisibility(View.GONE);
		}
	}


}
