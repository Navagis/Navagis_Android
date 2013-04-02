package com.navagis.models;

import android.content.ContentValues;

import com.navagis.database.DatabaseAssets.COLUMN;
import com.navagis.database.DatabaseAssets.TABLE;

public interface IRelianceModel {
	ContentValues toContentValues();
	TABLE getTable();
	Object getId();
	COLUMN getColumnId();
}