package com.navagis.dao.abstracts;

import java.util.List;

import com.navagis.models.Asset;

public interface IAssetDataSource {
	List<Asset> getAllAssets();
	Asset getAsset(Integer id);
}
