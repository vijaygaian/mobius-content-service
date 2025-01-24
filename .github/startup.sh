#!/bin/bash
set -e  
echo "1"

export service_name=mobius-content-service
export application_pkg=org.gaian
export app_path=/etc/conf/gaian/$service_name

rm -rf $app_path/bootstrap.properties

echo "spring.application.name=$service_name" >> $app_path/bootstrap.properties
echo "spring.cloud.consul.host=$consul_url" >> $app_path/bootstrap.properties
echo "spring.cloud.consul.port=$consul_port" >> $app_path/bootstrap.properties
echo "management.health.consul.enabled = false" >> $app_path/bootstrap.properties
echo "spring.cloud.consul.discovery.register = false" >> $app_path/bootstrap.properties
echo "spring.cloud.config.enabled = true" >> $app_path/bootstrap.properties
echo "spring.cloud.config.enabled = true" >> $app_path/bootstrap.properties
echo "spring.cloud.config.enabled.uri = http://$consul_url:$consul_port" >> $app_path/bootstrap.properties

export OTEL_PROPAGATORS="b3multi"
export OTEL_SERVICE_NAME="mobius-content-service-trace"
export OTEL_TRACES_EXPORTER="zipkin"
export OTEL_JAVAAGENT_DEBUG="false"
export OTEL_EXPORTER_ZIPKIN_ENDPOINT="http://zipkin.istio-system.svc.cluster.local:9411/api/v2/spans"
export OTEL_INSTRUMENTATION_HTTP_CAPTURE_REQUEST_HEADERS="x-b3-traceid"

export jvm_options=" $jvm_options -Dspring.profiles.active=ENVIRONMENT -Dspring.config.location=/etc/conf/gaian/$service_name/ -Dlogfile.location=/var/log/gaian/$service_name/$service_name.log -javaagent:opentelemetry-javaagent.jar";






















#export jvm_options=" $jvm_options -Dspring.profiles.active=ENVIRONMENT -Dspring.config.location=/etc/conf/gaian/$service_name/  -#Dlogfile.location=/var/log/gaian/$service_name/$service_name.log";

#if [ -z "$apm_url" ];
#then
#     echo "apm_url is empty"
#else
#     export jvm_options="$jvm_options -javaagent:/etc/skywalking-apm/skywalking-agent.jar \
#        -Dskywalking.collector.backend_service=$apm_url \
#        -Dskywalking.agent.service_name=$service_name \
#        -Dskywalking.logging.dir=/var/logs/ \
#        -Dskywalking.logging.max_file_size=100000
#        -Dskywalking.logging.max_history_files=2"
#fi

echo "Starting $service_name"

echo "jvm options : $jvm_options"

exec java $JAVA_OPTIONS $jvm_options -jar $service_name.jar

