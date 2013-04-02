package com.navagis.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.navagis.main.R;


public class DebugDialogFragment extends DialogFragment{
	private Spinner emailSpinner;
	private Spinner passSpinner;
	private CheckBox skipLogin;
	private DebugInterface di;
	private static DebugDialogFragment instance = new DebugDialogFragment();
	
	public static DebugDialogFragment getIntance() {
		return instance;
	}
	public void setDebugListener(DebugInterface debugInterface) {
		this.di = debugInterface;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.debug_dialog, null);
		emailSpinner = (Spinner) view.findViewById(R.id.emailSpinner);
		passSpinner = (Spinner) view.findViewById(R.id.passSpinner);
		skipLogin = (CheckBox) view.findViewById(R.id.skipLogin);
			    
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Debug Mode");
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton(R.string.action_sign_in, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();

				if(di != null)
				di.result(skipLogin.isChecked(), 
						getSpinnerValue(emailSpinner), 
						getSpinnerValue(passSpinner));
				
			}
			
		});
		builder.setView(view);
		return builder.create();

	}
	
	public interface DebugInterface {
		void result(boolean isSkipLogin, String email, String pass);
	}
	
	private String getSpinnerValue(Spinner sp) {
		String value = null;
		try {
			value = sp.getSelectedItem().toString();
		} catch (Exception e) {
			e.printStackTrace();
			value = "";
		}
		return value;
	}

}
