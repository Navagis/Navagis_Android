package com.reliance.dao.abstracts;

import java.util.List;

import com.reliance.models.Asset;

public interface IAssetDataSource {
	List<Asset> getAllAssets();
	Asset getAsset(Integer id);
}
