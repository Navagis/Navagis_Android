package com.reliance.daos.real;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.reliance.dao.abstracts.AbstractDataSource;
import com.reliance.dao.abstracts.IAlertDataSource;
import com.reliance.database.DatabaseAssets.COLUMN;
import com.reliance.database.DatabaseAssets.TABLE;
import com.reliance.models.Alert;
import com.reliance.utils.Util;

public class AlertDataSource extends AbstractDataSource implements IAlertDataSource{

	public List<Alert> getAllAlerts() {
		List<Alert> alerts = null;
		try {
			open();

			alerts = new ArrayList<Alert>();

			Cursor cursor = db.query(TABLE.ALERT.toString(),
					null, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Alert alert = cursorToAlert(cursor);
				alerts.add(alert);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return alerts;

	}

	public Alert getAlert(Integer id) {
		Alert alert = null;
		try {
			if(id == null) {
				throw new NullPointerException("Id required");
			}

			final String whereClause = COLUMN.ALERT_ID.toString()+" = "+id;

			Cursor cursor = db.query(TABLE.ALERT.toString(),
					null, whereClause, null, null, null, null);

			cursor.moveToFirst();
			alert = cursorToAlert(cursor);
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally {
			close();
		}
		return alert;
	}

	public void resetPacketId(Integer packetId) {
		try {
			ContentValues values = new ContentValues();
			values.putNull(COLUMN.PACKET_ID.name);
			String where = COLUMN.PACKET_ID + "=?";
			String[] whereArgs = new String[] {Integer.toString(packetId)};
			int result = db.update(
					TABLE.ALERT.toString(),
					values,
					where,
					whereArgs);
			
			if(result == -1) 
				Util.logE("ALERT NOT UPDATED");
			else 
				Util.logD(result+" ALERT/S UPDATED");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteAlertsWithPacketId(Integer packetId) {
		try
		{
			String whereClause = COLUMN.PACKET_ID.toString()+"=?";
			String[] whereArgs = new String[] {Integer.toString(packetId)};
			int result = db.delete(TABLE.ALERT.toString(), whereClause, whereArgs);
			if (result == -1) 
				Util.logE("ALERT COULD NOT BE DELETED");
			else
				Util.logD(result+" ALERT/S DELETED SUCCESSFULLY");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	private Alert cursorToAlert(Cursor cursor) {
		Integer alertId = cursor.getInt(cursor.getColumnIndex(COLUMN.ALERT_ID.toString()));
		String message = cursor.getString(cursor.getColumnIndex(COLUMN.MESSAGE.toString()));;
		String title = cursor.getString(cursor.getColumnIndex(COLUMN.TITLE.toString()));;
		String email = cursor.getString(cursor.getColumnIndex(COLUMN.ALERT_EMAIL.toString()));;
		String deviceId = cursor.getString(cursor.getColumnIndex(COLUMN.DEVICE_ID.toString()));;
		Integer assetId = cursor.getInt(cursor.getColumnIndex(COLUMN.ASSET_ID.toString()));;
		Integer driverId = cursor.getInt(cursor.getColumnIndex(COLUMN.USER_ID.toString()));;
		Long alertDateTime = cursor.getLong(cursor.getColumnIndex(COLUMN.ALERT_DATE_TIME.toString()));
		Integer packetId = cursor.getInt(cursor.getColumnIndex(COLUMN.PACKET_ID.toString()));

		Alert alert = new Alert(alertId, message, title, email, deviceId, assetId, driverId, alertDateTime, packetId);
		return alert;
	}

}
