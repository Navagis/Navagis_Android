package com.navagis.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.navagis.database.DatabaseAssets.TABLE;
import com.navagis.main.NavagisApplication;
import com.navagis.utils.Util;

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

		private static DatabaseHelper instance = new DatabaseHelper(NavagisApplication.getContext());
		private static final String DATABASE_NAME = "navagis.db";
		private static final int DATABASE_VERSION = 1;

		private DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		private static DatabaseHelper getInstance() {
			return instance;
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			for(String createQuery: DatabaseAssets.CREATE_QUERIES()) {
				database.execSQL(createQuery);
			}
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

			for(TABLE table: TABLE.values()) {
				db.execSQL(DatabaseAssets.DROP_TABLE_IF_EXISTS+ table.name);
			}

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
			for(String deleteQuery: DatabaseAssets.DELETE_QUERIES()) {
				db.execSQL(deleteQuery);
			}
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
