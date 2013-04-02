package com.reliance.dao.abstracts;

import java.util.List;

import com.reliance.models.MaterialDrops;

public interface IMaterialDropsDataSource {
	List<MaterialDrops> getAllMaterialDrops();
	MaterialDrops getMaterialDrops(Integer id);
	void insertPacketId(Integer packetId);
	void resetPacketId(Integer packetId);
	void deleteMaterialDropsWithPacketId(Integer packetId);
}
