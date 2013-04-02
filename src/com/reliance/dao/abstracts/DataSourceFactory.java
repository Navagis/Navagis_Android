package com.reliance.dao.abstracts;

import com.reliance.daos.real.AlertDataSource;
import com.reliance.daos.real.AssetDataSource;
import com.reliance.daos.real.AssetTrackingDataSource;
import com.reliance.daos.real.DeviceDataSource;
import com.reliance.daos.real.DriverDataSource;
import com.reliance.daos.real.MaterialDropsDataSource;
import com.reliance.database.DatabaseAssets.TABLE;

public final class DataSourceFactory {
	public static AbstractDataSource getRealDataSource(TABLE table) {
		switch(table) {
		case ALERT:
			return new AlertDataSource();
		case ASSET:
			return new AssetDataSource();
		case ASSET_TRACKING:
			return new AssetTrackingDataSource();
		case DEVICE:
			return new DeviceDataSource();
		case MATERIAL_DROPS:
			return new MaterialDropsDataSource();
		case USER:
			return new DriverDataSource();
		default:
			throw new RuntimeException("TABLE not found");
		}
	}

}
