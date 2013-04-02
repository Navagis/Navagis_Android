package com.navagis.dao.abstracts;

import java.util.List;

import com.navagis.models.Alert;

public interface IAlertDataSource {
	List<Alert> getAllAlerts();
	Alert getAlert(Integer id);
}
