package com.reliance.dao.abstracts;

import java.util.List;

import com.reliance.models.User;

public interface IDriverDataSource {
	List<User> getAllDrivers();
	User getDriver(Integer driverId);
}
