package com.navagis.models;

import android.content.ContentValues;

import com.navagis.database.DatabaseAssets.COLUMN;
import com.navagis.database.DatabaseAssets.TABLE;

public class Alert implements IRelianceModel {

	private Integer alertId;
	private String message;
	private String title;
	private String email;
	private String deviceId;
	private Integer asset_id;
	private Integer driverId;
	private Long alertDateTime;
	private Integer packetId;
	private String create_time;
	private double lat;
	private double lng;
	
	
	public Alert(Integer alertId, String message, String title, String email,
			String deviceId, Integer assetId, Integer driverId,
			Long alertDateTime, Integer packetId) {
		super();
		this.alertId = alertId;
		this.message = message;
		this.title = title;
		this.email = email;
		this.deviceId = deviceId;
		this.asset_id = assetId;
		this.driverId = driverId;
		this.alertDateTime = alertDateTime;
		this.setPacketId(packetId);
	}

	public Alert() {
	}

	public Integer getAlertId() {
		return alertId;
	}

	public void setAlertId(Integer alertId) {
		this.alertId = alertId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getAssetId() {
		return asset_id;
	}

	public void setAssetId(Integer assetId) {
		this.asset_id = assetId;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public Long getCreateDateTime() {
		return alertDateTime;
	}

	public void setCreateDateTime(Long createDateTime) {
		this.alertDateTime = createDateTime;
	}

	public String getCreateTime() {
		return create_time;
	}

	public void setCreateTime(String create_time) {
		this.create_time = create_time;
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
		cv.put(COLUMN.MESSAGE.toString(), message);
		cv.put(COLUMN.TITLE.toString(), title);
		cv.put(COLUMN.ALERT_EMAIL.toString(), email);
		cv.put(COLUMN.DEVICE_ID.toString(), deviceId);
		cv.put(COLUMN.ASSET_ID.toString(), asset_id);
		cv.put(COLUMN.USER_ID.toString(), driverId);
		cv.put(COLUMN.ALERT_DATE_TIME.toString(), alertDateTime);
		return cv;
	}

	@Override
	public TABLE getTable() {
		return TABLE.ALERT;
	}

	@Override
	public Object getId() {
		return alertId;
	}

	@Override
	public COLUMN getColumnId() {
		return COLUMN.ALERT_ID;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

}
