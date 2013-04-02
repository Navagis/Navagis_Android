package com.reliance.daos.real;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;

import com.reliance.dao.abstracts.AbstractDataSource;
import com.reliance.dao.abstracts.IDeviceDataSource;
import com.reliance.database.DatabaseAssets.COLUMN;
import com.reliance.database.DatabaseAssets.TABLE;
import com.reliance.models.Device;

public class DeviceDataSource extends AbstractDataSource implements IDeviceDataSource{

	@Override
	public List<Device> getAllDevices() {
		List<Device> devices = null;
		try {
			open();
			
			devices = new ArrayList<Device>();

			Cursor cursor = db.query(TABLE.ASSET.toString(),
					null, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Device device = cursorToDevice(cursor);
				devices.add(device);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return devices;
	}

	@Override
	public Device getDevice(String id) {
		Device device = null;
		try {
			open();
			
			if(id == null) {
				throw new NullPointerException("Id required");
			}
			
			final String whereClause = COLUMN.DEVICE_ID+" = "+id;
			
			Cursor cursor = db.query(TABLE.DEVICE.toString(),
					null, whereClause, null, null, null, null);

			cursor.moveToFirst();
			device = cursorToDevice(cursor);
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally {
			close();
		}
		return device;
	}

	private Device cursorToDevice(Cursor cursor) {
		String deviceId = cursor.getString(cursor.getColumnIndex(COLUMN.DEVICE_ID.toString()));
		String osVersion = cursor.getString(cursor.getColumnIndex(COLUMN.OS_VERSION.toString()));
		String deviceBrand = cursor.getString(cursor.getColumnIndex(COLUMN.DEVICE_BRAND.toString()));
		Long statusDateTime = cursor.getLong(cursor.getColumnIndex(COLUMN.STATUS_DATE_TIME.toString()));
		Integer assetId = cursor.getInt(cursor.getColumnIndex(COLUMN.ASSET_ID.toString()));

		Device device = new Device(deviceId, osVersion, deviceBrand, statusDateTime, assetId);
		return device;
	}

}
