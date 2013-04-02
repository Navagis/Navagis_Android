package com.reliance.main;


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
import com.reliance.api.IApiResponseHandler;
import com.reliance.api.ServerResponse;
import com.reliance.api.requests.ApiRequestTask;
import com.reliance.api.requests.IServerRequest;
import com.reliance.api.requests.MaterialDropsRequest;
import com.reliance.api.requests.WaypointsRequest;
import com.reliance.constants.DATETIME_FORMAT;
import com.reliance.constants.ICON;
import com.reliance.models.MaterialDrops;
import com.reliance.utils.LocationHandler;
import com.reliance.utils.Util;

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
		
		locationHandler = RelianceApplication.getLocationHandler();
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
    				RelianceApplication.stopBackgroundServices();
    			}
    		})
    		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    	    		MainActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    			}
    		}).create().show();
    		
		} else {
	    	RelianceApplication.startBackgroundServices();
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
			RelianceApplication.promptLogOutAlert();
			break;
		default:
			return false;
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		RelianceApplication.promptLogOutAlert();
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
			RelianceApplication.showErrorDialog(errorMessage);
			return false;
		} else {
			String curTime = Util.getCurrentDateTime(DATETIME_FORMAT.SERVER);

			materialDrops.setName(dropNameEt.getText().toString());
			materialDrops.setExtraInfo((extraInfo != null) ? extraInfo : "");
			materialDrops.setStoreAddress(storeSpinner.getSelectedItem().toString());
			materialDrops.setLat(locationHandler.getLatitude());
			materialDrops.setLng(locationHandler.getLongitude());
			materialDrops.setAsset_id(RelianceApplication.getAssetId());
			materialDrops.setCreateTime(curTime);
			
			Util.logD(storeSpinner.getSelectedItem().toString());
			return true;
		}
	}
	
	public void sendMaterialDrops() {
		
		if(!validateFields())
			return;
		else if(!RelianceApplication.isNetworkAvailable()) {
			RelianceApplication.showErrorDialog("No network connection");
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
		int assetId = RelianceApplication.getAssetId();
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
					RelianceApplication.showErrorDialog("Unable to connect to the server"); 
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
					RelianceApplication.showAlertDialog("No Stores Found", "Unable to find store locations for this asset");
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
				
				if(result == null) {RelianceApplication.showErrorDialog("Unable to connect to the server"); return;};
				
				if(result.isSuccess()){
					clearFields();
					RelianceApplication.showToast("Success");										
				} else {
					RelianceApplication.showErrorDialog(result.getErrorMessage());
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
