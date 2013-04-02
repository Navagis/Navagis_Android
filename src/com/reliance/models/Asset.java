package com.reliance.models;

import android.content.ContentValues;

import com.reliance.database.DatabaseAssets.COLUMN;
import com.reliance.database.DatabaseAssets.TABLE;

public class Asset implements IRelianceModel {
	private Integer assetId;
	private String assetType;
	private String assetName;
	private Integer routeId;
	private Integer driverId;
	private String mobileDeviceId;
	private Long checkInTime;
	private Long checkOutTime;

	public Integer getAssetId() {
		return assetId;
	}

	
	public Asset(Integer assetId, String assetType, String assetName,
			Integer routeId, Integer driverId, String mobileDeviceId,
			Long checkInTime, Long checkOutTime) {
		super();
		this.assetId = assetId;
		this.assetType = assetType;
		this.assetName = assetName;
		this.routeId = routeId;
		this.driverId = driverId;
		this.mobileDeviceId = mobileDeviceId;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}


	public Asset() {
		this(null, null, null, null, null, null, null, null);
	}


	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public String getDeviceId() {
		return mobileDeviceId;
	}

	public void setDeviceId(String deviceId) {
		this.mobileDeviceId = deviceId;
	}

	public Long getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Long checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Long getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(Long checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN.ASSET_NAME.toString(), assetName);
		cv.put(COLUMN.ASSET_TYPE.toString(), assetType);
		cv.put(COLUMN.ROUTE_ID.toString(), routeId);
		cv.put(COLUMN.USER_ID.toString(), driverId);
		cv.put(COLUMN.DEVICE_ID.toString(), mobileDeviceId);
		cv.put(COLUMN.CHECK_IN_DATE_TIME.toString(), checkInTime);
		cv.put(COLUMN.CHECK_OUT_DATE_TIME.toString(), checkOutTime);
		return cv;
	}

	@Override
	public TABLE getTable() {
		return TABLE.ASSET;
	}

	@Override
	public Object getId() {
		return assetId;
	}

	@Override
	public COLUMN getColumnId() {
		return COLUMN.ASSET_ID;
	}

}
