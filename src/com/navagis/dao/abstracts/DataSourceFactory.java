package com.navagis.dao.abstracts;

import com.navagis.daos.real.AlertDataSource;
import com.navagis.daos.real.AssetDataSource;
import com.navagis.daos.real.AssetTrackingDataSource;
import com.navagis.daos.real.DeviceDataSource;
import com.navagis.daos.real.DriverDataSource;
import com.navagis.daos.real.MaterialDropsDataSource;
import com.navagis.database.DatabaseAssets.TABLE;

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
