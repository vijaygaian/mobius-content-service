
package com.aidtaas.mobius.content.services.config;

import jakarta.servlet.MultipartConfigElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Slf4j
@Configuration
public class MultipartConfig {

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    log.info("-----MultipartConfig Element Bean Creation.");
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofGigabytes(50));
    factory.setMaxRequestSize(DataSize.ofGigabytes(50));
    return factory.createMultipartConfig();
  }

}
