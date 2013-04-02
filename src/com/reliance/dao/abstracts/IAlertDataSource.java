package com.reliance.dao.abstracts;

import java.util.List;

import com.reliance.models.Alert;

public interface IAlertDataSource {
	List<Alert> getAllAlerts();
	Alert getAlert(Integer id);
}
