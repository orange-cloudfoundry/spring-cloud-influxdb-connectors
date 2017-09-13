package com.orange.spring.cloud.connector.influxdb.cloudfoundry;

import com.orange.spring.cloud.connector.influxdb.core.service.InfluxDBServiceInfo;
import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import org.springframework.cloud.util.UriInfo;

import java.util.Map;

/**
 *
 * @author Sebastien Bortolussi
 *
 */
public class InfluxDBServiceInfoCreator extends CloudFoundryServiceInfoCreator<InfluxDBServiceInfo> {

    private static final int DEFAULT_INFLUXDB_PORT = 8086;
    private static final String INFLUXDB_TAG = "influxdb";
    public static final String USER_PROVIDED_SERVICE_LABEL = "user-provided";

    public InfluxDBServiceInfoCreator() {
        // the literal in the tag is CloudFoundry-specific
        super(new Tags( INFLUXDB_TAG));
    }

    @Override
    public boolean accept(Map<String, Object> serviceData) {
        return (isUserProvidedService(serviceData) && nameStartsWithTag(serviceData)) || super.accept(serviceData);
    }

    protected boolean nameStartsWithTag(Map<String, Object> serviceData) {
        String serviceId = getId(serviceData);
        return serviceId.startsWith(this.INFLUXDB_TAG);
    }

    protected boolean isUserProvidedService(Map<String, Object> serviceData) {
        String label = (String)serviceData.get("label");
        return label.equals(USER_PROVIDED_SERVICE_LABEL);
    }

    public InfluxDBServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        String id = getId(serviceData);

        Map<String, Object> credentials = getCredentials(serviceData);

        String uriScheme = getUriSchemeFromCredentials(credentials);

        String host = getStringFromCredentials(credentials, "host", "hostname");

        int port = getIntFromCredentials(credentials, "port");
        if (port == -1) {
            port = DEFAULT_INFLUXDB_PORT;
        }

        String username = getStringFromCredentials(credentials, "user", "username");
        String password = getStringFromCredentials(credentials, "password");

        String uri = new UriInfo(uriScheme, host, port, username, password).toString();

        String dbName = getStringFromCredentials(credentials, "dbname");

        return new InfluxDBServiceInfo(id, uri, dbName);

    }

    private String getUriSchemeFromCredentials(Map<String, Object> serviceData) {
        String uri = getUriFromCredentials(serviceData);
        UriInfo uriInfo = new UriInfo(uri);
        return uriInfo.getScheme();
    }

}
