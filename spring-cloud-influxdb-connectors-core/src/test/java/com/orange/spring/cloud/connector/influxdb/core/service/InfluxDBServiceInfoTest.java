package com.orange.spring.cloud.connector.influxdb.core.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InfluxDBServiceInfoTest {

    @Test
    public void getDbName() throws Exception {
        InfluxDBServiceInfo serviceInfo = new InfluxDBServiceInfo("influx-1", "http://user:password@hostname:8086", "my-influxdb");

        assertEquals("my-influxdb", serviceInfo.getDbName());
    }

}