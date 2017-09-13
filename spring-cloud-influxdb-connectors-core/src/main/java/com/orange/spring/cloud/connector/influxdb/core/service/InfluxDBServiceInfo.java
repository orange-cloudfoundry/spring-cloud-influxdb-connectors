package com.orange.spring.cloud.connector.influxdb.core.service;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 *
 * @author Sebastien Bortolussi
 *
 */
@ServiceLabel("influxdb")
public class InfluxDBServiceInfo extends UriBasedServiceInfo {

	private String dbName;

	public InfluxDBServiceInfo(String id, String uri, String dbName) {
		super(id, uri);
		this.dbName = dbName;

	}

	public String getDbName() {
		return dbName;
	}
}
