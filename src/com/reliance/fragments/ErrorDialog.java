package com.reliance.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.reliance.constants.ICON;
import com.reliance.utils.Util;

public class ErrorDialog extends DialogFragment {
	
	public static ErrorDialog getInstance(String title, String error) {			
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", error);
		
		ErrorDialog dialog = new ErrorDialog();
		dialog.setArguments(args);
		
		return dialog;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		String error = getArguments().getString("message");
		
		if(title == null) 
			title = "Error";
		if(error == null)
			error = "";
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle(title)
		.setMessage(error)
		.setIcon(Util.getIcon(ICON.ALERT))
		.setNegativeButton(android.R.string.ok, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		});

		return alert.create();
	}
}
