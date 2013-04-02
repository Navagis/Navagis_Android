package com.reliance.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reliance.database.DatabaseAssets.TABLE;
import com.reliance.main.RelianceApplication;
import com.reliance.utils.Util;

public class DatabaseManager{

	public static SQLiteDatabase getWritableDb() {
		return DatabaseHelper.getInstance().getWritableDatabase();
	}

	/**
	 * Handles the creation of database tables.  
	 * Databases are stored in  "/data/data/package/databases/DATABASE_NAME"
	 * 
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper{

		private static DatabaseHelper instance = new DatabaseHelper(RelianceApplication.getContext());
		private static final String DATABASE_NAME = "reliance.db";
		private static final int DATABASE_VERSION = 1;

		private DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		private static DatabaseHelper getInstance() {
			return instance;
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(DatabaseAssets.CREATE_ALERT);
			database.execSQL(DatabaseAssets.CREATE_ASSET);
			database.execSQL(DatabaseAssets.CREATE_ASSET_TRACKING);
			database.execSQL(DatabaseAssets.CREATE_USER);
			database.execSQL(DatabaseAssets.CREATE_DEVICE);
			database.execSQL(DatabaseAssets.CREATE_DROPS);

		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			if (!db.isReadOnly()) {
				// Enable foreign key constraints
				db.execSQL("PRAGMA foreign_keys=ON;");
				//				db.setLockingEnabled(true);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, 
				int newVersion) 
		{
			Util.logD("Upgrading database from version " + oldVersion 
					+ " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS "+ TABLE.ALERT);
			db.execSQL("DROP TABLE IF EXISTS "+ TABLE.ASSET);
			db.execSQL("DROP TABLE IF EXISTS "+ TABLE.ASSET_TRACKING);
			db.execSQL("DROP TABLE IF EXISTS "+ TABLE.USER);
			db.execSQL("DROP TABLE IF EXISTS "+ TABLE.DEVICE);
			db.execSQL("DROP TABLE IF EXISTS "+ TABLE.MATERIAL_DROPS);

			onCreate(db);
		}
	}

	/**
	 * Deletes all the entries in all the tables
	 */
	public static void emptyAllTables()
	{	
		SQLiteDatabase db = getWritableDb();
		try {
			db.beginTransaction();
			db.execSQL(DatabaseAssets.DELETE_ASSETS);
			db.execSQL(DatabaseAssets.DELETE_ALERTS);
			db.execSQL(DatabaseAssets.DELETE_ASSET_TRACKINGS);
			db.execSQL(DatabaseAssets.DELETE_USERS);
			db.execSQL(DatabaseAssets.DELETE_MATERIAL_DROPS);
			db.setTransactionSuccessful();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}


}
