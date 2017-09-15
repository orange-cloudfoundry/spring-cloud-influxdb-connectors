# spring-cloud-influxdb-connectors
[![Build Status](https://travis-ci.org/orange-cloudfoundry/spring-cloud-influxdb-connectors.svg)](https://travis-ci.org/orange-cloudfoundry/spring-cloud-influxdb-connectors)
[![Apache Version 2 Licence](http://img.shields.io/:license-Apache%20v2-blue.svg)](LICENSE)
[ ![Download](https://api.bintray.com/packages/elpaaso/maven/spring-cloud-influxdb-connectors/images/download.svg) ](https://bintray.com/elpaaso/maven/spring-cloud-influxdb-connectors/_latestVersion)


[![Join the chat at https://gitter.im/orange-cloudfoundry/elpaaso](https://img.shields.io/badge/gitter-join%20chat%20→-brightgreen.svg)](https://gitter.im/orange-cloudfoundry/elpaaso?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Spring cloud influxDB service connectors to use influxDB in CloudFoundry.

**You can find an example app here: https://github.com/orange-cloudfoundry/influxdb-connectors-poc**

## Service Detection

The connectors will check for an existing bound service with:

- tags including ``influxdb``

- label beginning with the ``influxdb`` tag


The connectors will also check for an existing bound user-provided service with:

- name beginning with the ``influxdb`` tag


## Getting started

### Import repositories to your project

**Maven**:

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-elpaaso-maven</id>
        <name>bintray</name>
        <url>http://dl.bintray.com/elpaaso/maven</url>
    </repository>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>central</id>
        <name>bintray</name>
        <url>http://jcenter.bintray.com</url>
    </repository>
</repositories>
```

**Gradle**:

```gradle
repositories {
    // ...
    jcenter()
    maven { url "http://dl.bintray.com/elpaaso/maven" }
}
```
### Add a connector to your project

First, make a version propertie:

**Maven**:

```xml
<properties>
    <influxdb.connectors.version>1.0.6</s3.connectors.version>
</properties>
```

**Gradle**:

```gradle
ext {
	$influxDBConnectorsVersion = "1.0.6"
}
```

### Add a connector to your project

#### Cloud Foundry

**Maven**:

```xml
<dependency>
    <groupId>com.orange.spring.cloud.connectors</groupId>
    <artifactId>spring-cloud-influxdb-connectors-cloudfoundry</artifactId>
    <version>${influxdb.connectors.version}</version>
</dependency>
```

**Gradle**:

```gradle
dependencies {
  compile("com.orange.spring.cloud.connectors:spring-cloud-influxdb-connectors-cloudfoundry:$influxDBConnectorsVersion")
}
```

### Use it !

Here the bootstrap for your spring boot app to get a `InfluxDB` java client which will help you to manipulate your influxDB database:


Usage example:

```java
@Component
public class ScheduledInfluxDBExporter {

    Logger logger = Logger.getLogger(ScheduledInfluxDBExporter.class);
    
    @Autowired
    private InfluxDB influxDB;

    @Scheduled(fixedRate = 5000)
    public void sendData() throws Exception {
        logger.debug("sending data to influxdb");
        
        Random rand = new Random();
        int value = rand.nextInt(10);

        Point point = Point.measurement("a_metric")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", value)
                .build();
        this.influxDB.write(point);
    }
}
```

### Deploy and run

#### Cloud Foundry

1. Create an influxDB service
Since no influxDB service offering seems to be available in any marketplace, you can use an influxDB service by creating the following user provided service.

```shell
$cf cups influxdb-service -p '{"dbname": "<DB_NAME>", "hostname": "<HOSTNAME>", "password": "<PASSWORD>", "port": "<PORT>", "uri": "<URI>", "username": "<USERNAME>"}'
```

2. Push your app with `cf push`
3. After the app has been pushed bind your new created service to your app (e.g: `cf bs nameofmyapp influxdb-service`)
4. Restage your app: `cf restage nameofmyapp`

#### Heroku

Not supported

#### Locally

Not supported

## Examples

You can find an example app here: https://github.com/orange-cloudfoundry/influxdb-connectors-poc

## Contributing

Report any issues or pull request on this repo.
