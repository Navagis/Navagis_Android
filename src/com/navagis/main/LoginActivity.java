package com.navagis.main;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.navagis.api.IApiResponseHandler;
import com.navagis.api.ServerResponse;
import com.navagis.api.requests.ApiRequestTask;
import com.navagis.api.requests.LoginRequest;
import com.navagis.constants.Constants;
import com.navagis.main.R;
import com.navagis.models.User;
import com.navagis.utils.DebugDialogFragment;
import com.navagis.utils.Util;
import com.navagis.utils.DebugDialogFragment.DebugInterface;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */

	// Values for email and password at the time of the login attempt.
	private String mAssetId;
	private String mAssetName;

	// UI references.
	private EditText mAssetIdView;
	private EditText mAssetNameView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private boolean skipLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mAssetIdView = (EditText) findViewById(R.id.asset_id);
		mAssetIdView.setText(mAssetId);

		mAssetNameView = (EditText) findViewById(R.id.asset_name);
		mAssetNameView
		.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.asset_name || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

//		setDebugDialog();
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		// Hide keyboard
		Util.hideKeyboard(mAssetNameView);

		// Reset errors.
		mAssetIdView.setError(null);
		mAssetNameView.setError(null);

		// Store values at the time of the login attempt.
		mAssetId = mAssetIdView.getText().toString();
		mAssetName = mAssetNameView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mAssetName)) {
			mAssetNameView.setError(getString(R.string.error_field_required));
			focusView = mAssetNameView;
			cancel = true;
		} 

		// Check for a valid email address.
		if (TextUtils.isEmpty(mAssetId)) {
			mAssetIdView.setError(getString(R.string.error_field_required));
			focusView = mAssetIdView;
			cancel = true;
		} 
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			
			try {
				loginTask();
			} catch (JSONException e) {
				showProgress(false);
				e.printStackTrace();
				RelianceApplication.showErrorDialog("Internal error occured: "+e.getMessage());
			}
		}
	}

	private void loginTask() throws JSONException {
		if (!RelianceApplication.isNetworkAvailable()) {
			RelianceApplication.showErrorDialog(getString(R.string.error_no_connection));
			return;
		}

		JSONObject jsRequest = new JSONObject();
		jsRequest.put(Constants.KEY_ASSET_ID, Integer.valueOf(mAssetId));
		jsRequest.put(Constants.KEY_ASSET_NAME, mAssetName);
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setJsonRequest(jsRequest);
		
		new ApiRequestTask(new IApiResponseHandler() {
			
			@Override
			public void onResponse(ServerResponse result) {
				showProgress(false);
				
				if(result == null) {RelianceApplication.showErrorDialog("Unable to connect to the server"); return;};
				
				if(result.isSuccess()){
					RelianceApplication.saveAsset(Integer.valueOf(mAssetId), mAssetName);
					
					RelianceApplication.startActivity(MainActivity.class);
										
				} else {
					
					mAssetNameView.setError(getString(R.string.error_incorrect_asset_name));
					mAssetIdView.setError(getString(R.string.error_incorrect_asset_id));
					
					mAssetNameView.requestFocus();
					Util.logE(result.getErrorMessage());
				}
					
			}
			
			@Override
			public void onPreExecute() {
				showProgress(true);
			}
		}).execute(loginRequest);
		
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(
					show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(
					show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	public void registerUser() {
		User user = new User();
		user.setEmail(mAssetId);
		user.setPassword(mAssetName);

		RelianceApplication.registerUser(user);

	}

	private void setDebugDialog() {
		DebugDialogFragment ddf = DebugDialogFragment.getIntance();
		ddf.setDebugListener(new DebugInterface() {

			@Override
			public void result(boolean isSkipLogin, String foreman, String crewCode) {
				setSkipLogin(isSkipLogin);
				mAssetIdView.setText(foreman);
				mAssetNameView.setText(crewCode);

				attemptLogin();
			}
		});

		ddf.show(getFragmentManager(), null);
	}

	public boolean isSkipLogin() {
		return skipLogin;
	}

	public void setSkipLogin(boolean skipLogin) {
		this.skipLogin = skipLogin;
	}

}
