package com.orange.spring.cloud.service.influxdb;

import com.orange.spring.cloud.connector.influxdb.core.service.InfluxDBServiceInfo;
import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Simplified access to creating InfluxDB database client.
 *
 * @author Sebastien Bortolussi
 */
public class InfluxDBServiceConnectorCreator extends AbstractServiceConnectorCreator<InfluxDB, InfluxDBServiceInfo> {

    @Override
    public InfluxDB create(InfluxDBServiceInfo serviceInfo, ServiceConnectorConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        InfluxDB influxDB = InfluxDBFactory.connect(serviceInfo.getUri(), serviceInfo.getUserName(), serviceInfo.getPassword(),builder);
        influxDB.setDatabase(serviceInfo.getDbName());
        return influxDB;
    }
}
