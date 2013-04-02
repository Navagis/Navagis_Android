package com.navagis.dao.abstracts;

import java.util.List;

import com.navagis.models.AssetTracking;

public interface IAssetTrackingDataSource {
	List<AssetTracking> getAllAssetTrackings();
	AssetTracking getAssetTracking(Integer id);
	void insertPacketId(Integer packetId);
	void resetPacketId(Integer packetId);
	void deleteAssetTracksWithPacketId(Integer packetId);
}
