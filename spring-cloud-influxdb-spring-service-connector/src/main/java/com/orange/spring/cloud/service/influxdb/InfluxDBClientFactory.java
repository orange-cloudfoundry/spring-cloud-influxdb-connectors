package com.orange.spring.cloud.service.influxdb;

import org.influxdb.InfluxDB;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Spring factory bean for influxdb service.
 *
 * @author Sebastien Bortolussi
 *
 */
public class InfluxDBClientFactory extends AbstractCloudServiceConnectorFactory<InfluxDB> {
	public InfluxDBClientFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, InfluxDB.class, serviceConnectorConfiguration);
	}
}
