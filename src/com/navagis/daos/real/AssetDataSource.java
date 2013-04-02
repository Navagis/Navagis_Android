package com.navagis.daos.real;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;

import com.navagis.dao.abstracts.AbstractDataSource;
import com.navagis.dao.abstracts.IAssetDataSource;
import com.navagis.database.DatabaseAssets.COLUMN;
import com.navagis.database.DatabaseAssets.TABLE;
import com.navagis.models.Asset;

public class AssetDataSource extends AbstractDataSource implements IAssetDataSource{
	
	public List<Asset> getAllAssets() {
		List<Asset> assets = null;
		try {
			open();
			
			assets = new ArrayList<Asset>();

			Cursor cursor = db.query(TABLE.ASSET.toString(),
					null, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Asset asset = cursorToAsset(cursor);
				assets.add(asset);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return assets;
	}

	public Asset getAsset(Integer id) {
		Asset asset = null;
		try {
			open();
			
			if(id == null) {
				throw new NullPointerException("Id required");
			}
			
			final String whereClause = COLUMN.ASSET_ID.toString()+" = "+id;
			Cursor cursor = db.query(TABLE.ASSET.toString(),
					null, whereClause, null, null, null, null);

			cursor.moveToFirst();
			asset = cursorToAsset(cursor);
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally {
			close();
		}
		return asset;
	}
	
	private Asset cursorToAsset(Cursor cursor) {
		 Integer assetId = cursor.getInt(cursor.getColumnIndex(COLUMN.ASSET_ID.toString()));
		 String assetType = cursor.getString(cursor.getColumnIndex(COLUMN.ASSET_TYPE.toString()));
		 String assetName = cursor.getString(cursor.getColumnIndex(COLUMN.ASSET_NAME.toString()));
		 Integer routeId = cursor.getInt(cursor.getColumnIndex(COLUMN.ROUTE_ID.toString()));
		 Integer driverId = cursor.getInt(cursor.getColumnIndex(COLUMN.USER_ID.toString()));
		 String mobileDeviceId = cursor.getString(cursor.getColumnIndex(COLUMN.DEVICE_ID.toString()));
		 Long checkInTime = cursor.getLong(cursor.getColumnIndex(COLUMN.CHECK_IN_DATE_TIME.toString()));
		 Long checkOutTime = cursor.getLong(cursor.getColumnIndex(COLUMN.CHECK_OUT_DATE_TIME.toString()));
		 
		 Asset asset = new Asset(assetId, assetType, assetName, routeId, driverId, mobileDeviceId, checkInTime,checkOutTime);
		 return asset;
	}

}
