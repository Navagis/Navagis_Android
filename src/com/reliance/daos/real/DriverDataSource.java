package com.reliance.daos.real;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;

import com.reliance.dao.abstracts.AbstractDataSource;
import com.reliance.dao.abstracts.IDriverDataSource;
import com.reliance.database.DatabaseAssets.COLUMN;
import com.reliance.database.DatabaseAssets.TABLE;
import com.reliance.models.User;

public class DriverDataSource extends AbstractDataSource implements IDriverDataSource{

	@Override
	public List<User> getAllDrivers() {
		List<User> users = null;
		try {			
			users = new ArrayList<User>();

			Cursor cursor = db.query(TABLE.USER.toString(),
					null, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				User user = cursorToDriver(cursor);
				users.add(user);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		} finally {
		}

		return users;
	}

	@Override
	public User getDriver(Integer id) {
		User user = null;
		try {
			if(id == null) {
				throw new NullPointerException("Id required");
			}
			
			final String whereClause = COLUMN.USER_ID+" = "+id;
			
			Cursor cursor = db.query(TABLE.USER.toString(),
					null, whereClause, null, null, null, null);

			cursor.moveToFirst();
			user = cursorToDriver(cursor);
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}	finally {
		}
		return user;
	}

	private User cursorToDriver(Cursor cursor) {
		String firstName = cursor.getString(cursor.getColumnIndex(COLUMN.FIRST_NAME.toString()));
		String lastName = cursor.getString(cursor.getColumnIndex(COLUMN.LAST_NAME.toString()));
		String email = cursor.getString(cursor.getColumnIndex(COLUMN.USER_EMAIL.toString()));
		String password = cursor.getString(cursor.getColumnIndex(COLUMN.PASSWORD.toString()));
		String driverId = cursor.getString(cursor.getColumnIndex(COLUMN.USER_ID.toString()));
		
		User user = new User(firstName, lastName, email, password, driverId);
		return user;
	}

}
