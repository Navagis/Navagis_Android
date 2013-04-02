package com.reliance.models;

import android.content.ContentValues;

import com.reliance.database.DatabaseAssets.COLUMN;
import com.reliance.database.DatabaseAssets.TABLE;

public interface IRelianceModel {
	ContentValues toContentValues();
	TABLE getTable();
	Object getId();
	COLUMN getColumnId();
}