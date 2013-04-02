package com.navagis.dao.abstracts;

import java.util.List;

import com.navagis.models.MaterialDrops;

public interface IMaterialDropsDataSource {
	List<MaterialDrops> getAllMaterialDrops();
	MaterialDrops getMaterialDrops(Integer id);
	void insertPacketId(Integer packetId);
	void resetPacketId(Integer packetId);
	void deleteMaterialDropsWithPacketId(Integer packetId);
}
