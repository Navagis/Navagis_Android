package com.reliance.dao.abstracts;

import java.util.List;

import com.reliance.models.Device;

public interface IDeviceDataSource {
	List<Device> getAllDevices();
	Device getDevice(String deviceId);
}
