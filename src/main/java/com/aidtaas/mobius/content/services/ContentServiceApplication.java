package com.aidtaas.mobius.content.services;

import com.aidtaas.mobius.blob.storage.config.NginxProperties;
import com.aidtaas.mobius.content.service.layer.properties.ContentHostProperties;
import com.aidtaas.mobius.content.service.layer.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableCaching
@SpringBootApplication(scanBasePackages = {"com.aidtaas.mobius.cache.wrapper.lib.config",
    "com.aidtaas.mobius.content.services",
    "com.aidtaas.mobius.error.services", "com.aidtaas.mobius.blob.storage",
    "com.aidtaas.mobius.acl.lib.config","com.aidtaas.mobius.acl.lib.utils", "com.aidtaas.mobius.content.service.layer",
    "com.aidtaas.mobius.content.service.layer.repo",
    "com.aidtaas.mobius.content.service.layer.service", "com.aidtaas.mobius.tracer.lib",
    "com.aidtaas.mobius.kafka.wrapper.lib.kafka.producer",
    "com.aidtaas.mobius.mongo.wrapper.lib"}, exclude = {DataSourceAutoConfiguration.class,
    ElasticsearchRestClientAutoConfiguration.class})
@EnableConfigurationProperties({FileStorageProperties.class, NginxProperties.class,
    ContentHostProperties.class})

@EnableMongoRepositories(basePackages = "com.aidtaas.mobius.content.service.layer.repo")
public class ContentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ContentServiceApplication.class, args);
  }
}