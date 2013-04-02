package com.navagis.models;

import android.content.ContentValues;

import com.navagis.database.DatabaseAssets.COLUMN;
import com.navagis.database.DatabaseAssets.TABLE;

public class User implements IRelianceModel{
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String userId;
	
	public User(String firstName, String lastName, String email,
			String password, String userId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.userId = userId;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN.USER_EMAIL.toString(), email);
		cv.put(COLUMN.FIRST_NAME.toString(), firstName);
		cv.put(COLUMN.LAST_NAME.toString(), lastName);
		cv.put(COLUMN.PASSWORD.toString(), password);
		return cv;
	}

	@Override
	public TABLE getTable() {
		return TABLE.USER;
	}

	@Override
	public Object getId() {
		return email;
	}

	@Override
	public COLUMN getColumnId() {
		return COLUMN.USER_EMAIL;
	}

}
