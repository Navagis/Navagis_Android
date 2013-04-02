package com.navagis.dao.abstracts;

import java.util.List;

import com.navagis.models.Device;

public interface IDeviceDataSource {
	List<Device> getAllDevices();
	Device getDevice(String deviceId);
}
