package com.reliance.dao.abstracts;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.reliance.database.DatabaseAssets.TABLE;
import com.reliance.database.DatabaseManager;
import com.reliance.models.IRelianceModel;
import com.reliance.utils.Util;
public abstract class AbstractDataSource{

	protected SQLiteDatabase db;	

	public void open() throws SQLException {
		db = DatabaseManager.getWritableDb();
	}

	public void close() {
		db.close();
	}


	public void insertRow(IRelianceModel data) {
		try {
			
//			if(doesRecordExist(data)) {
//				updateRow(data);
//				Util.logD("Record "+data.getTable()+" "+data.getId());
//				return;
//			} 
			
				ContentValues values = data.toContentValues();
				long id = db.insertOrThrow(data.getTable().toString(),
						null, values);
				if (id == -1) 
					Util.logE(data.getTable()+" COULD NOT BE INSERTED");
				else
					Util.logE(data.getTable()+" "+id+" INSERTED SUCCESSFULLY");
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}

	}


	public void updateRow(IRelianceModel data) {
		try {

			db.beginTransaction();
			
			ContentValues values = data.toContentValues();		
			final String whereClause = data.getColumnId() + "= ?";
			final String[] args = {(String) data.getId()};
			
			int id = db.update(data.getTable().toString(), values, whereClause, args);
			if (id == -1) 
				Util.logE(data.getTable()+" COULD NOT BE UPDATED");
			else
				Util.logE(data.getTable()+" "+id+" UPDATED SUCCESSFULLY");
			
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally {
			db.endTransaction();
		}

	}


	public void deleteRow(IRelianceModel data) {
		try {
			
			final String whereClause = data.getColumnId().toString()+ " = " + data.getId();
			int id = db.delete(data.getTable().toString(),whereClause, null);
			if (id == -1) 
				Util.logE(data.getTable()+" COULD NOT BE DELETED");
			else
				Util.logE(data.getTable()+" "+id+" RECORD/S DELETED SUCCESSFULLY");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
	}


	public void deleteAll(TABLE table) {
		try {		
			db.beginTransaction();
			
			int id = db.delete(table.toString(),null, null);
			if (id == -1) 
				Util.logE(table.toString()+" COULD NOT BE DELETED");
			else
				Util.logE(table.toString()+" "+id+" RECORD/S DELETED SUCCESSFULLY");
			
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			db.endTransaction();
		}
	}

	public void insertAll(List<? extends IRelianceModel> models) {
		for(IRelianceModel data: models) {
			insertRow(data);
		}
	}

	/**
	 * This function checks whether a record already exists in the database
	 * @return true if record exists or false otherwise
	 */
	public boolean doesRecordExist(IRelianceModel data) {
		boolean exists = false;

		final String where = data.getColumnId() + "= ?";
		final String[] args = {(String) data.getId()};
		Cursor cursor = null;
		try {
			cursor = db.query(
					data.getTable().toString(),
					new String[] {(String) data.getColumnId().toString()},
					where,
					args,
					null, null, null);

			if(cursor == null) exists = false;
			
			if (cursor.moveToNext())
				exists = true;

			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return exists;
	}

}
