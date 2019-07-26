## Geo IP checks Hive UDF

### Description
Checks if ip is part of subnet

### To use
1. Upload jar file to Hdfs
2. Register udf with something like this:
    CREATE TEMPORARY FUNCTION geoip AS 'sergey.dashko.udf.GeoIpUDF' USING JAR 'hdfs://192.168.56.101:8020/user/cloudera/geoip_hive_udf-assembly-0.1.jar';
3. Use it in your query:
    geoip('10.2.5.0/24', '10.2.5.3')
