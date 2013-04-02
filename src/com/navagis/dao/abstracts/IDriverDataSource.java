package com.navagis.dao.abstracts;

import java.util.List;

import com.navagis.models.User;

public interface IDriverDataSource {
	List<User> getAllDrivers();
	User getDriver(Integer driverId);
}
