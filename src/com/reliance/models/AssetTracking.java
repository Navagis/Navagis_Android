package com.reliance.models;

import android.content.ContentValues;

import com.reliance.database.DatabaseAssets.COLUMN;
import com.reliance.database.DatabaseAssets.TABLE;

public class AssetTracking implements IRelianceModel {
	private Integer assetTrackingId;
	private Double lat;
	private Double lng;
	private String create_time;
	private Integer asset_id;
	private Integer packetId;
	
	
	public AssetTracking(Integer assetTrackingId, Double latitude,
			Double longitude, String createTime, Integer assetId, Integer packetId) {
		super();
		this.assetTrackingId = assetTrackingId;
		this.lat = latitude;
		this.lng = longitude;
		this.create_time = createTime;
		this.asset_id = assetId;
		this.packetId = packetId;
	}

	public AssetTracking() {
	}

	public Integer getAssetTrackingId() {
		return assetTrackingId;
	}

	public void setAssetTrackingId(Integer assetTrackingId) {
		this.assetTrackingId = assetTrackingId;
	}

	public Double getLatitude() {
		return lat;
	}

	public void setLatitude(Double latitude) {
		this.lat = latitude;
	}

	public Double getLongitude() {
		return lng;
	}

	public void setLongitude(Double longitude) {
		this.lng = longitude;
	}

	public String getTrackingDateTime() {
		return create_time;
	}

	public void setTrackingDateTime(String createTime) {
		this.create_time = createTime;
	}

	public Integer getAssetId() {
		return asset_id;
	}

	public void setAssetId(Integer assetId) {
		this.asset_id = assetId;
	}

	public Integer getPacketId() {
		return packetId;
	}

	public void setPacketId(Integer packetId) {
		this.packetId = packetId;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN.LATITUDE.toString(), lat);
		cv.put(COLUMN.LONGITUDE.toString(), lng);
		cv.put(COLUMN.TRACKING_DATE_TIME.toString(), create_time);
		cv.put(COLUMN.ASSET_ID.toString(), asset_id);
		cv.put(COLUMN.PACKET_ID.toString(), packetId);
		return cv;
	}

	@Override
	public TABLE getTable() {
		return TABLE.ASSET_TRACKING;
	}

	@Override
	public Object getId() {
		return assetTrackingId;
	}

	@Override
	public COLUMN getColumnId() {
		return COLUMN.ASSET_TRACKING_ID;
	}

}
