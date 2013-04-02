package com.reliance.daos.real;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.reliance.dao.abstracts.AbstractDataSource;
import com.reliance.dao.abstracts.IAssetTrackingDataSource;
import com.reliance.database.DatabaseAssets.COLUMN;
import com.reliance.database.DatabaseAssets.TABLE;
import com.reliance.main.RelianceApplication;
import com.reliance.models.AssetTracking;
import com.reliance.utils.Util;

public class AssetTrackingDataSource extends AbstractDataSource implements IAssetTrackingDataSource{

	@Override
	public List<AssetTracking> getAllAssetTrackings() {
		List<AssetTracking> assets = null;
		try {
			db.beginTransaction();
			assets = new ArrayList<AssetTracking>();

			Cursor cursor = db.query(TABLE.ASSET_TRACKING.toString(),
					null, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				AssetTracking assetTracking = cursorToAssetTracking(cursor);
				assets.add(assetTracking);
				cursor.moveToNext();
			}
			cursor.close();
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

		return assets;
	}

	@Override
	public AssetTracking getAssetTracking(Integer id) {
		AssetTracking assetTracking = null;
		try {			
			if(id == null) {
				throw new NullPointerException("Id required");
			}
			final String whereClause = COLUMN.ASSET_TRACKING_ID+" = "+id;
			Cursor cursor = db.query(TABLE.ASSET_TRACKING.toString(),
					null, whereClause, null, null, null, null);

			cursor.moveToFirst();
			assetTracking = cursorToAssetTracking(cursor);
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally {
		}
		return assetTracking;
	}

	public void insertPacketId(Integer packetId) {
		try {
			int assetId = RelianceApplication.getAssetId();
			
			ContentValues values = new ContentValues();
			values.put(COLUMN.PACKET_ID.name, packetId);
			String where = COLUMN.ASSET_ID + "=?";
			String[] whereArgs = new String[] {Integer.toString(assetId)};
			int result = db.update(
					TABLE.ASSET_TRACKING.toString(),
					values,
					where,
					whereArgs);

			if(result == -1) 
				Util.logE("ASSET TRACKS NOT UPDATED");
			else 
				Util.logD(result+" TRACK/S UPDATED");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void resetPacketId(Integer packetId) {
		try {
			ContentValues values = new ContentValues();
			values.putNull(COLUMN.PACKET_ID.name);
			String where = COLUMN.PACKET_ID + "=?";
			String[] whereArgs = new String[] {Integer.toString(packetId)};
			int result = db.update(
					TABLE.ASSET_TRACKING.toString(),
					values,
					where,
					whereArgs);

			if(result == -1) 
				Util.logE("ASSET TRACKS NOT UPDATED");
			else 
				Util.logD(result+" TRACK/S UPDATED");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteAssetTracksWithPacketId(Integer packetId) {
		try
		{
			String whereClause = COLUMN.PACKET_ID.toString()+"=?";
			String[] whereArgs = new String[] {Integer.toString(packetId)};
			int result = db.delete(TABLE.ASSET_TRACKING.toString(), whereClause, whereArgs);
			if (result == -1) 
				Util.logE("ASSET TRACKS COULD NOT BE DELETED");
			else
				Util.logD(result+" TRACK/S DELETED SUCCESSFULLY");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	private AssetTracking cursorToAssetTracking(Cursor cursor) {
		Integer assetTrackingId = cursor.getInt(cursor.getColumnIndex(COLUMN.ASSET_TRACKING_ID.toString()));
		Double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN.LATITUDE.toString()));
		Double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN.LONGITUDE.toString()));
		String trackingDateTime = cursor.getString(cursor.getColumnIndex(COLUMN.TRACKING_DATE_TIME.toString()));
		Integer assetId = cursor.getInt(cursor.getColumnIndex(COLUMN.ASSET_ID.toString()));
		Integer packetId = cursor.getInt(cursor.getColumnIndex(COLUMN.PACKET_ID.toString()));

		AssetTracking asstrck = new AssetTracking(assetTrackingId, latitude, longitude, trackingDateTime, assetId, packetId);
		return asstrck;
	}
}
