package com.navagis.daos.real;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.navagis.dao.abstracts.AbstractDataSource;
import com.navagis.dao.abstracts.IMaterialDropsDataSource;
import com.navagis.database.DatabaseAssets.COLUMN;
import com.navagis.database.DatabaseAssets.TABLE;
import com.navagis.main.NavagisApplication;
import com.navagis.models.MaterialDrops;
import com.navagis.utils.Util;

public class MaterialDropsDataSource extends AbstractDataSource implements IMaterialDropsDataSource{

	@Override
	public List<MaterialDrops> getAllMaterialDrops() {
		List<MaterialDrops> drops = null;
		try {
			db.beginTransaction();
			drops = new ArrayList<MaterialDrops>();

			Cursor cursor = db.query(TABLE.MATERIAL_DROPS.toString(),
					null, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				MaterialDrops materialDrops = cursorToMaterialDrops(cursor);
				drops.add(materialDrops);
				cursor.moveToNext();
			}
			cursor.close();
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

		return drops;
	}

	@Override
	public MaterialDrops getMaterialDrops(Integer id) {
		MaterialDrops materialDrops = null;
		try {			
			if(id == null) {
				throw new NullPointerException("Id required");
			}
			final String whereClause = COLUMN.DROP_ID+" = "+id;
			Cursor cursor = db.query(TABLE.MATERIAL_DROPS.toString(),
					null, whereClause, null, null, null, null);

			cursor.moveToFirst();
			materialDrops = cursorToMaterialDrops(cursor);
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally {
		}
		return materialDrops;
	}

	@Override
	public void insertPacketId(Integer packetId) {
		try {
			int assetId = NavagisApplication.getAssetId();
			
			ContentValues values = new ContentValues();
			values.put(COLUMN.PACKET_ID.name, packetId);
			String where = COLUMN.ASSET_ID + "=?";
			String[] whereArgs = new String[] {Integer.toString(assetId)};
			int result = db.update(
					TABLE.MATERIAL_DROPS.toString(),
					values,
					where,
					whereArgs);

			if(result == -1) 
				Util.logE("MATERIAL DROPS NOT UPDATED");
			else 
				Util.logD(result+" DROP/S UPDATED");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resetPacketId(Integer packetId) {
		try {
			ContentValues values = new ContentValues();
			values.putNull(COLUMN.PACKET_ID.name);
			String where = COLUMN.PACKET_ID + "=?";
			String[] whereArgs = new String[] {Integer.toString(packetId)};
			int result = db.update(
					TABLE.MATERIAL_DROPS.toString(),
					values,
					where,
					whereArgs);

			if(result == -1) 
				Util.logE("MATERIAL DROPS NOT UPDATED");
			else 
				Util.logD(result+" DROP/S UPDATED");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteMaterialDropsWithPacketId(Integer packetId) {
		try
		{
			String whereClause = COLUMN.PACKET_ID.toString()+"=?";
			String[] whereArgs = new String[] {Integer.toString(packetId)};
			int result = db.delete(TABLE.MATERIAL_DROPS.toString(), whereClause, whereArgs);
			if(result == -1) 
				Util.logE("MATERIAL DROPS NOT UPDATED");
			else 
				Util.logD(result+" DROP/S UPDATED");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private MaterialDrops cursorToMaterialDrops(Cursor cursor) {
		Integer dropId = cursor.getInt(cursor.getColumnIndex(COLUMN.DROP_ID.toString()));
		String dropName = cursor.getString(cursor.getColumnIndex(COLUMN.DROP_NAME.toString()));
		String dropCreateTime = cursor.getString(cursor.getColumnIndex(COLUMN.DROP_CREATE_TIME.toString()));
		String dropExtra = cursor.getString(cursor.getColumnIndex(COLUMN.DROP_EXTRA.toString()));
		String storeName = cursor.getString(cursor.getColumnIndex(COLUMN.STORE.toString()));
		Integer assetId = cursor.getInt(cursor.getColumnIndex(COLUMN.ASSET_ID.toString()));
		Integer packetId = cursor.getInt(cursor.getColumnIndex(COLUMN.PACKET_ID.toString()));

		MaterialDrops drops = new MaterialDrops(dropName, assetId, dropCreateTime, storeName, dropExtra, dropId, packetId, null, null, null);
		return drops;
	}
	
	

}
