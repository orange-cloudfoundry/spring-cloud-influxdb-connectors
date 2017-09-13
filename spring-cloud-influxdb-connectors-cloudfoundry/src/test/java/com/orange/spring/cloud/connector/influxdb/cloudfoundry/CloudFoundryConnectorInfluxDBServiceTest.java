package com.orange.spring.cloud.connector.influxdb.cloudfoundry;

import com.orange.spring.cloud.connector.influxdb.core.service.InfluxDBServiceInfo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import org.springframework.cloud.service.ServiceInfo;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * 
 * @author Sebastien Bortolussi
 *
 */
public class CloudFoundryConnectorInfluxDBServiceTest extends AbstractCloudFoundryConnectorTest {

	protected static final String DBNAME = "my-metrics";
	private static final String HTTPS_SCHEME = "https";
	private static final String HTTP_SCHEME = "http";

	@Test
	public void influxDBServiceInfoCreationHttpScheme() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(getInfluxDBServicePayload("influx-1", hostname, port, username, password, DBNAME, HTTP_SCHEME)));


		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo influxDBServiceInfo = getServiceInfo(serviceInfos, "influx-1");
		assertServiceFoundOfType(influxDBServiceInfo, InfluxDBServiceInfo.class);

		assertUriBasedServiceInfoFields(influxDBServiceInfo, HTTP_SCHEME, hostname, port, username, password, null);

		assertDBNamelEqual(influxDBServiceInfo,DBNAME);
	}

	@Test
	public void influxDBServiceInfoCreationHttpsScheme() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(getInfluxDBServicePayload("influx-1", hostname, port, username, password, DBNAME, HTTPS_SCHEME)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo influxDBServiceInfo = getServiceInfo(serviceInfos, "influx-1");
		assertServiceFoundOfType(influxDBServiceInfo, InfluxDBServiceInfo.class);

		assertUriBasedServiceInfoFields(influxDBServiceInfo, HTTPS_SCHEME, hostname, port, username, password, null);

		assertDBNamelEqual(influxDBServiceInfo,DBNAME);

	}

	@Test
	public void influxDBServiceInfoCreationFromUserProvidedServiceWithNameStartingWithRequiredTag() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(getInfluxDBUserProvidedServicePayload("influxdb-service", hostname, port, username, password, DBNAME, HTTPS_SCHEME)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo influxDBServiceInfo = getServiceInfo(serviceInfos, "influxdb-service");
		assertServiceFoundOfType(influxDBServiceInfo, InfluxDBServiceInfo.class);

		assertDBNamelEqual(influxDBServiceInfo,DBNAME);
		assertUriBasedServiceInfoFields(influxDBServiceInfo, HTTPS_SCHEME, hostname, port, username, password, null);

	}

	@Test
	public void NoInfluxDBServiceInfoCreationFromUserProvidedServiceWithNameNotStartingWithRequiredTag() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(getInfluxDBUserProvidedServicePayload("notmatching", hostname, port, username, password, DBNAME, HTTPS_SCHEME)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo influxDBServiceInfo = getServiceInfo(serviceInfos, "notmatching");

		assertNoServiceFoundOfType(influxDBServiceInfo, InfluxDBServiceInfo.class);

	}

	protected void assertDBNamelEqual(ServiceInfo serviceInfo, String dbname) {
		assertThat(serviceInfo, instanceOf(InfluxDBServiceInfo.class));
		assertEquals(dbname, ((InfluxDBServiceInfo) serviceInfo).getDbName());
	}

	protected static void assertNoServiceFoundOfType(ServiceInfo serviceInfo, Class<? extends ServiceInfo> type) {
		Assert.assertNotNull(serviceInfo);
		Assert.assertThat(serviceInfo, CoreMatchers.not(CoreMatchers.instanceOf(type)));
	}


	private String getInfluxDBServicePayload(String serviceName, String hostname, int port,
											 String user, String password, String dbname, String scheme) {
		String payload = readTestDataFile("test-influxdb-service-info.json");
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", String.valueOf(port));
		payload = payload.replace("$username", user);
		payload = payload.replace("$password", password);
		payload = payload.replace("$dbname", dbname);
		payload = payload.replace("$scheme", scheme);


		return payload;
	}

	private String getInfluxDBUserProvidedServicePayload(String serviceName, String hostname, int port,
														 String user, String password, String dbname, String scheme) {
		String payload = readTestDataFile("test-influxdb-user-provided-service-info.json");
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", String.valueOf(port));
		payload = payload.replace("$username", user);
		payload = payload.replace("$password", password);
		payload = payload.replace("$dbname", dbname);
		payload = payload.replace("$scheme", scheme);


		return payload;
	}
}
