package com.navagis.models;

import android.content.ContentValues;

import com.navagis.database.DatabaseAssets.COLUMN;
import com.navagis.database.DatabaseAssets.TABLE;

public class MaterialDrops implements IRelianceModel {
	String name;
	Integer asset_id;
	String create_time;
	String store_name;
	String extra_info;
	Integer drop_id;
	Integer packetId;
	Double lat;
	Double lng;
	String store_address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(Integer asset_id) {
		this.asset_id = asset_id;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreateTime(String create_time) {
		this.create_time = create_time;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStoreName(String store_name) {
		this.store_name = store_name;
	}

	public String getStoreAddress() {
		return store_address;
	}

	public void setStoreAddress(String store_address) {
		this.store_address = store_address;
	}

	public String getExtra_info() {
		return extra_info;
	}

	public void setExtraInfo(String extra_info) {
		this.extra_info = extra_info;
	}

	public Integer getDrop_id() {
		return drop_id;
	}

	public void setDrop_id(Integer drop_id) {
		this.drop_id = drop_id;
	}

	public Integer getPacketId() {
		return packetId;
	}

	public void setPacketId(Integer packetId) {
		this.packetId = packetId;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public MaterialDrops() {
	}

	public MaterialDrops(String name, Integer asset_id, String create_time,
			String store_name, String extra_info, Integer drop_id,
			Integer packetId, Double lat, Double lng, String store_address) {
		super();
		this.name = name;
		this.asset_id = asset_id;
		this.create_time = create_time;
		this.store_name = store_name;
		this.extra_info = extra_info;
		this.drop_id = drop_id;
		this.packetId = packetId;
		this.lat = lat;
		this.lng = lng;
		this.store_address = store_address;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN.DROP_ID.toString(), drop_id);
		cv.put(COLUMN.DROP_NAME.toString(), name);
		cv.put(COLUMN.DROP_EXTRA.toString(), extra_info);
		cv.put(COLUMN.DROP_CREATE_TIME.toString(), create_time);
		cv.put(COLUMN.ASSET_ID.toString(), asset_id);
		cv.put(COLUMN.STORE.toString(), store_name);
		cv.put(COLUMN.PACKET_ID.toString(), packetId);
		return cv;
	}

	@Override
	public TABLE getTable() {
		return TABLE.MATERIAL_DROPS;
	}

	@Override
	public Object getId() {
		return drop_id;
	}

	@Override
	public COLUMN getColumnId() {
		return COLUMN.DROP_ID;
	}

}
