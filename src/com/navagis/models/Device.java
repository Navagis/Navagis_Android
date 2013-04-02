package com.navagis.models;

import android.content.ContentValues;

import com.navagis.database.DatabaseAssets.COLUMN;
import com.navagis.database.DatabaseAssets.TABLE;

public class Device implements IRelianceModel {

	private String deviceId;
	private String osVersion;
	private String deviceBrand;
	private Long statusDateTime;
	private Integer assetId;
	
	public Device(String deviceId, String osVersion, String deviceBrand,
			Long statusDateTime, Integer assetId) {
		super();
		this.deviceId = deviceId;
		this.osVersion = osVersion;
		this.deviceBrand = deviceBrand;
		this.statusDateTime = statusDateTime;
		this.assetId = assetId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getDeviceBrand() {
		return deviceBrand;
	}

	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}

	public Long getStatusDateTime() {
		return statusDateTime;
	}

	public void setStatusDateTime(Long statusDateTime) {
		this.statusDateTime = statusDateTime;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN.DEVICE_ID.toString(), deviceId);
		cv.put(COLUMN.OS_VERSION.toString(), osVersion);
		cv.put(COLUMN.DEVICE_BRAND.toString(), deviceBrand);
		cv.put(COLUMN.STATUS_DATE_TIME.toString(), statusDateTime);
		cv.put(COLUMN.ASSET_ID.toString(), assetId);
		return cv;
	}

	@Override
	public TABLE getTable() {
		return TABLE.DEVICE;
	}

	@Override
	public Object getId() {
		return deviceId;
	}

	@Override
	public COLUMN getColumnId() {
		return COLUMN.DEVICE_ID;
	}

}
