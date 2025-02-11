# Stage 1: Build the application
FROM --platform=linux/amd64 gaianmobius/openjdk-21-mvn-3.9.6 as builder

# Set build arguments
ARG SPRING_APPLICATION_NAME
ARG NEXUS_URL
ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD
ARG SONAR_HOST
ARG SONAR_TOKEN
ARG DEPLOYMENT_PLATFORM
ARG SONAR_SCAN

# Prepare Maven settings.xml with Nexus configuration
RUN mkdir -p /root/.m2 \
 && echo "<?xml version='1.0' encoding='UTF-8'?> \
<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
      xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
      xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
                          https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
    <servers> \
      <server> \
        <id>nexus</id> \
        <username>${NEXUS_USERNAME}</username> \
        <password>${NEXUS_PASSWORD}</password> \
      </server> \
    </servers> \
    <mirrors> \
        <mirror> \
            <id>nexus</id> \
            <name>nexus</name>\
            <mirrorOf>nexus</mirrorOf> \
            <url>${NEXUS_URL}</url> \
        </mirror> \
    </mirrors> \
</settings>" > /root/.m2/settings.xml

# Pre-download Maven dependencies
COPY pom.xml /build/pom.xml
RUN mkdir -p /build/src 
 # && mvn -f /build/pom.xml dependency:resolve

# Copy the remaining project files
COPY src /build/src

# Change working directory
WORKDIR /build

Build the project
RUN if [ "$SONAR_SCAN" = false ]; then \
        mvn clean install -DskipTests; \
    elif [ "$SONAR_SCAN" = true ]; then \
        mvn clean install -DskipTests -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
        -Dsonar.projectKey=${SPRING_APPLICATION_NAME} \
        -Dsonar.projectName=${SPRING_APPLICATION_NAME} \
        -Dsonar.host.url=${SONAR_HOST} \
        -Dsonar.login=${SONAR_TOKEN} \
        -Dsonar.qualitygate.wait=true; \
    fi
# RUN mvn clean install -DskipTests
# Rename the JAR file
RUN for file in /build/target/${SPRING_APPLICATION_NAME}*.jar; do \
    if [ -e "$file" ] && [ "$file" != "/build/target/${SPRING_APPLICATION_NAME}.jar" ]; then \
        mv "$file" /build/target/${SPRING_APPLICATION_NAME}.jar && ls -lart /build/target && break; \
    fi; \
    done
RUN ls -lart /build/target

# Clean up Maven settings and source files to reduce image size
RUN rm -rf /root/.m2/settings.xml \
 && rm -rf /build/src /build/pom.xml

# Stage 2: Runtime environment
FROM eclipse-temurin:21-jre-alpine

# Install necessary debugging tools
RUN apk update && apk add --no-cache openjdk21 curl && rm -rf /var/cache/apk/*

# Environment variables
ARG SPRING_APPLICATION_NAME
ARG SPRING_CLOUD_CONSUL_HOST
ARG SPRING_CLOUD_CONSUL_PORT
ARG CONSUL_ENVIRONMENT
ARG MAINTAINER_EMAIL
ARG JVM_OPTIONS

ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME} \
    SPRING_CLOUD_CONSUL_HOST=${SPRING_CLOUD_CONSUL_HOST} \
    SPRING_CLOUD_CONSUL_PORT=${SPRING_CLOUD_CONSUL_PORT} \
    APP_PATH=/etc/conf/gaian/${SPRING_APPLICATION_NAME} \
    APPLICATION_PKG=com.gaian \
    OTEL_PROPAGATORS="b3multi" \
    OTEL_SERVICE_NAME="${SPRING_APPLICATION_NAME}-trace" \
    OTEL_TRACES_EXPORTER="zipkin" \
    OTEL_JAVAAGENT_DEBUG="false" \
    OTEL_EXPORTER_ZIPKIN_ENDPOINT="http://zipkin.istio-system.svc.cluster.local:9411/api/v2/spans" \
    OTEL_INSTRUMENTATION_HTTP_CAPTURE_REQUEST_HEADERS="x-b3-traceid" \
    JVM_OPTIONS=${JVM_OPTIONS}

# Copy the JAR from the builder stage
COPY --from=builder /build/target/${SPRING_APPLICATION_NAME}.jar /${SPRING_APPLICATION_NAME}.jar

# Copy additional configuration files
COPY src/main/resources/logback-spring.xml /etc/conf/gaian/${SPRING_APPLICATION_NAME}/logback-spring.xml

# Expose the application port
EXPOSE 8080

# Start the application
ENTRYPOINT ["sh", "-c", "java $JVM_OPTIONS -jar ${SPRING_APPLICATION_NAME}.jar"]


# # Stage 1: Build the application
# FROM --platform=linux/amd64 gaianmobius/openjdk-21-mvn-3.9.6 as builder

# # Update Nexus Repository details to the correct URL
# ARG SPRING_APPLICATION_NAME
# ARG NEXUS_URL
# ARG NEXUS_USERNAME
# ARG NEXUS_PASSWORD
# ARG SONAR_HOST
# ARG SONAR_TOKEN
# ARG DEPLOYMENT_PLATFORM
# ARG SONAR_SCAN

# # Prepare Maven settings.xml with the correct Nexus URL and Central repository (Public repository)
# RUN mkdir -p /root/.m2 \
#  && echo "<?xml version='1.0' encoding='UTF-8'?> \
# <settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
#       xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
#       xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
#                           https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
#     <servers> \
#       <server> \
#         <id>nexus</id> \
#         <username>${NEXUS_USERNAME}</username> \
#         <password>${NEXUS_PASSWORD}</password> \
#       </server> \
#     </servers> \
#     <mirrors> \
#         <mirror> \
#             <id>nexus</id> \
#             <name>nexus</name>\
#             <mirrorOf>nexus</mirrorOf> \
#             <url>${NEXUS_URL}</url> \
#             <blocked>false</blocked>\
#         </mirror> \
#     </mirrors> \
# </settings>" > /root/.m2/settings.xml

# # Copy the .m2 repository from runner Cache(Git Workflow) to Docker
# COPY build/dependency/repository /root/.m2/repository


# # Copy the project files from Repositery to the container
# COPY pom.xml /build/pom.xml
# COPY src /build/src


# # Change container working directory
# WORKDIR /build


# # Conditional build steps based on DEPLOYMENT_PLATFORM
# RUN if [ "$SONAR_SCAN" = false ]; then \
#         mvn clean install  -f pom.xml; \
#     elif [ "$SONAR_SCAN" = true ]; then \
#         mvn clean install  -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SPRING_APPLICATION_NAME} -Dsonar.projectName=${SPRING_APPLICATION_NAME} -Dsonar.host.url=${SONAR_HOST} -Dsonar.login=${SONAR_TOKEN} -Dsonar.qualitygate.wait=true -f pom.xml; \
#     fi

# # Renaming JAR File for Spring Application
# RUN for file in /build/target/${SPRING_APPLICATION_NAME}*.jar; do \
#     if [ -e "$file" ] && [ "$file" != "/build/target/${SPRING_APPLICATION_NAME}.jar" ]; then \
#         mv "$file" /build/target/${SPRING_APPLICATION_NAME}.jar && break; \
#     fi; \
#     done

# # Removing the Project data from container
# RUN rm -rf /root/.m2/settings.xml
# RUN rm -rf /build/pom.xml
# RUN rm -rf /build/src


# # Stage 2: Setup the runtime environment

# FROM eclipse-temurin:21-jre-alpine

# # Install jmap and other debug tools
# RUN apk update && apk add --no-cache openjdk21 && apk add --no-cache curl && rm -rf /var/cache/apk/*


# # Environment variables for the application
# ARG SPRING_APPLICATION_NAME
# ARG SPRING_CLOUD_CONSUL_HOST
# ARG SPRING_CLOUD_CONSUL_PORT
# ARG CONSUL_ENVIRONMENT
# ARG MAINTAINER_EMAIL
# ARG JVM_OPTIONS

# # Set maintainer
# LABEL maintainer="${MAINTAINER_EMAIL}"

# # Set environment variables
# ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME} \
#     SPRING_CLOUD_CONSUL_HOST=${SPRING_CLOUD_CONSUL_HOST} \
#     SPRING_CLOUD_CONSUL_PORT=${SPRING_CLOUD_CONSUL_PORT} \
#     APP_PATH=/etc/conf/gaian/${SPRING_APPLICATION_NAME} \
#     APPLICATION_PKG=com.gaian \
#     OTEL_PROPAGATORS="b3multi" \
#     OTEL_SERVICE_NAME="${SPRING_APPLICATION_NAME}-trace" \
#     OTEL_TRACES_EXPORTER="zipkin" \
#     OTEL_JAVAAGENT_DEBUG="false" \
#     OTEL_EXPORTER_ZIPKIN_ENDPOINT="http://zipkin.istio-system.svc.cluster.local:9411/api/v2/spans" \
#     OTEL_INSTRUMENTATION_HTTP_CAPTURE_REQUEST_HEADERS="x-b3-traceid" \
#     JVM_OPTIONS=${JVM_OPTIONS}

# # Copy the build jar file from builder (State 1)
# COPY --from=builder /build/target/${SPRING_APPLICATION_NAME}.jar /${SPRING_APPLICATION_NAME}.jar

# # Copy the src for Logback Configuration
# COPY src src

# # Copy the docker for OpenTelemetry jar
# COPY docker docker

# # Conditional Relocation of Logback Configuration
# RUN if [ -s src/main/resources/logback-spring.xml ]; then \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME} && \
#         mv src/main/resources/logback-spring.xml /etc/conf/gaian/${SPRING_APPLICATION_NAME}/logback-spring.xml; \
#     else \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME}; \
#     fi

# # Conditional Relocation of OpenTelemetry jar
# RUN if [ -s docker/opentelemetry-javaagent.jar ]; then \
#         mkdir -p /var/log/GC-Analyzer && \
#         touch /var/log/GC-Analyzer/gc.log && \
#         mv docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar; \
#     else \
#          echo "Skipping OpenTelemetry Java agent configuration - file not found"; \
#     fi

# # Removing the Project data from container
# RUN rm -rf src docker

# # Adding OpenTelemetry Jar
# ADD docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
# RUN mkdir -p /var/log/GC-Analyzer && touch /var/log/GC-Analyzer/gc.log

# # Expose the application port
# EXPOSE 8080


# # Start the Java application
# ENTRYPOINT ["sh", "-c", "java $JVM_OPTIONS -jar ${SPRING_APPLICATION_NAME}.jar"]



# # Stage 1: Build the application
# FROM --platform=linux/amd64 gaianmobius/openjdk-21-mvn-3.9.6 as builder

# # Update Nexus Repository details to the correct URL
# ARG SPRING_APPLICATION_NAME
# ARG NEXUS_URL
# ARG NEXUS_USERNAME
# ARG NEXUS_PASSWORD
# ARG SONAR_HOST
# ARG SONAR_TOKEN
# ARG DEPLOYMENT_PLATFORM
# ARG SONAR_SCAN

# # Prepare Maven settings.xml with the correct Nexus URL and Central repository (Public repository)
# RUN mkdir -p /root/.m2 \
#  && echo "<?xml version='1.0' encoding='UTF-8'?> \
# <settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
#       xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
#       xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
#                           https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
#     <servers> \
#       <server> \
#         <id>nexus</id> \
#         <username>${NEXUS_USERNAME}</username> \
#         <password>${NEXUS_PASSWORD}</password> \
#       </server> \
#     </servers> \
#     <mirrors> \
#         <mirror> \
#             <id>nexus</id> \
#             <name>nexus</name>\
#             <mirrorOf>nexus</mirrorOf> \
#             <url>${NEXUS_URL}</url> \
#             <blocked>false</blocked>\
#         </mirror> \
#     </mirrors> \
# </settings>" > /root/.m2/settings.xml

# # Copy the .m2 repository from runner Cache(Git Workflow) to Docker
# COPY build/dependency/repository /root/.m2/repository


# # Copy the project files from Repositery to the container
# COPY pom.xml /build/pom.xml
# COPY src /build/src


# # Change container working directory
# WORKDIR /build


# # Conditional build steps based on DEPLOYMENT_PLATFORM
# RUN if [ "$SONAR_SCAN" = false ]; then \
#         mvn clean install -DskipTests -f pom.xml; \
#     elif [ "$SONAR_SCAN" = true ]; then \
#         mvn clean install -DskipTests -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SPRING_APPLICATION_NAME} -Dsonar.projectName=${SPRING_APPLICATION_NAME} -Dsonar.host.url=${SONAR_HOST} -Dsonar.login=${SONAR_TOKEN} -Dsonar.qualitygate.wait=true -f pom.xml; \
#     fi

# # Renaming JAR File for Spring Application
# RUN for file in /build/target/${SPRING_APPLICATION_NAME}*.jar; do \
#     if [ -e "$file" ] && [ "$file" != "/build/target/${SPRING_APPLICATION_NAME}.jar" ]; then \
#         mv "$file" /build/target/${SPRING_APPLICATION_NAME}.jar && break; \
#     fi; \
#     done

# # Removing the Project data from container
# RUN rm -rf /root/.m2/settings.xml
# RUN rm -rf /build/pom.xml
# RUN rm -rf /build/src


# # Stage 2: Setup the runtime environment

# FROM eclipse-temurin:21-jre-alpine

# # Install jmap and other debug tools
# RUN apk update && apk add --no-cache openjdk21 && apk add --no-cache curl && rm -rf /var/cache/apk/*


# # Environment variables for the application
# ARG SPRING_APPLICATION_NAME
# ARG SPRING_CLOUD_CONSUL_HOST
# ARG SPRING_CLOUD_CONSUL_PORT
# ARG CONSUL_ENVIRONMENT
# ARG MAINTAINER_EMAIL
# ARG JVM_OPTIONS

# # Set maintainer
# # LABEL maintainer="${MAINTAINER_EMAIL}"

# # Set environment variables
# ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME} \
#     SPRING_CLOUD_CONSUL_HOST=${SPRING_CLOUD_CONSUL_HOST} \
#     SPRING_CLOUD_CONSUL_PORT=${SPRING_CLOUD_CONSUL_PORT} \
#     APP_PATH=/etc/conf/gaian/${SPRING_APPLICATION_NAME} \
#     APPLICATION_PKG=com.gaian \
#     OTEL_PROPAGATORS="b3multi" \
#     OTEL_SERVICE_NAME="${SPRING_APPLICATION_NAME}-trace" \
#     OTEL_TRACES_EXPORTER="zipkin" \
#     OTEL_JAVAAGENT_DEBUG="false" \
#     OTEL_EXPORTER_ZIPKIN_ENDPOINT="http://zipkin.istio-system.svc.cluster.local:9411/api/v2/spans" \
#     OTEL_INSTRUMENTATION_HTTP_CAPTURE_REQUEST_HEADERS="x-b3-traceid" \
#     JVM_OPTIONS=${JVM_OPTIONS}

# # Copy the build jar file from builder (State 1)
# COPY --from=builder /build/target/${SPRING_APPLICATION_NAME}.jar /${SPRING_APPLICATION_NAME}.jar

# # Copy the src for Logback Configuration
# COPY src src

# # Copy the docker for OpenTelemetry jar
# COPY docker docker

# # Conditional Relocation of Logback Configuration
# RUN if [ -s src/main/resources/logback-spring.xml ]; then \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME} && \
#         mv src/main/resources/logback-spring.xml /etc/conf/gaian/${SPRING_APPLICATION_NAME}/logback-spring.xml; \
#     else \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME}; \
#     fi

# # Conditional Relocation of OpenTelemetry jar
# RUN if [ -s docker/opentelemetry-javaagent.jar ]; then \
#         mkdir -p /var/log/GC-Analyzer && \
#         touch /var/log/GC-Analyzer/gc.log && \
#         mv docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar; \
#     else \
#          echo "Skipping OpenTelemetry Java agent configuration - file not found"; \
#     fi

# # Removing the Project data from container
# RUN rm -rf src docker

# # Adding OpenTelemetry Jar
# ADD docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
# RUN mkdir -p /var/log/GC-Analyzer && touch /var/log/GC-Analyzer/gc.log

# # Expose the application port
# EXPOSE 8080

# # Start the Java application
# ENTRYPOINT ["sh", "-c", "java $JVM_OPTIONS -jar ${SPRING_APPLICATION_NAME}.jar"]

## Stage 1: Build the application
#FROM --platform=linux/amd64 gaianmobius/openjdk-21-mvn-3.9.6 as builder
#
## Update Nexus Repository details to the correct URL
#ARG SPRING_APPLICATION_NAME
#ARG NEXUS_URL
#ARG NEXUS_USERNAME
#ARG NEXUS_PASSWORD
#ARG SONAR_HOST
#ARG SONAR_TOKEN
#ARG DEPLOYMENT_PLATFORM
#
## Prepare Maven settings.xml with the correct Nexus URL and Central repository (Public repository)
#RUN mkdir -p /root/.m2 && \
#    echo "<?xml version='1.0' encoding='UTF-8'?> \
#    <settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
#        xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
#        xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
#                            https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
#        <servers> \
#            <server> \
#                <id>nexus</id> \
#                <username>${NEXUS_USERNAME}</username> \
#                <password>${NEXUS_PASSWORD}</password> \
#            </server> \
#        </servers> \
#        <mirrors> \
#            <mirror> \
#                <id>nexus</id> \
#                <name>nexus</name>\
#                <mirrorOf>nexus</mirrorOf> \
#                <url>${NEXUS_URL}</url> \
#                <blocked>false</blocked>\
#            </mirror> \
#        </mirrors> \
#    </settings>" > /root/.m2/settings.xml
#
## Copy the .m2 repository from runner Cache(Git Workflow) to Docker
#COPY build/dependency/repository /root/.m2/repository
#
## Copy the project files from Repository to the container
#COPY pom.xml /build/
#COPY src/main /build/src/main
#COPY docker/criu.sh /build/criu.sh
#RUN chmod +x /build/criu.sh
#
## Change container working directory
#WORKDIR /build
#
#
## Conditional build steps based on DEPLOYMENT_PLATFORM
#RUN if [ "$DEPLOYMENT_PLATFORM" = "dev" ]; then \
#        mvn clean install -DskipTests -f pom.xml; \
#    elif [ "$DEPLOYMENT_PLATFORM" = "prod" ]; then \
#        # mvn clean install -DskipTests -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SPRING_APPLICATION_NAME} -Dsonar.projectName=${SPRING_APPLICATION_NAME} -Dsonar.host.url=${SONAR_HOST} -Dsonar.login=${SONAR_TOKEN} -Dsonar.qualitygate.wait=true -f pom.xml; \
#        mvn clean install -DskipTests -f pom.xml; \
#    fi
#
## Renaming JAR File for Spring Application
#RUN for file in /build/target/${SPRING_APPLICATION_NAME}*.jar; do \
#    if [ -e "$file" ] && [ "$file" != "/build/target/${SPRING_APPLICATION_NAME}.jar" ]; then \
#        mv "$file" /build/target/${SPRING_APPLICATION_NAME}.jar && break; \
#    fi; \
#done
#
## Stage 2: Setup the runtime environment
#FROM --platform=linux/amd64 gaianmobius/criu:v3.19
#
## Environment variables for the application
#ARG SPRING_APPLICATION_NAME
#ARG SPRING_CLOUD_CONSUL_HOST
#ARG SPRING_CLOUD_CONSUL_PORT
#ARG CONSUL_ENVIRONMENT
#ARG MAINTAINER_EMAIL
#ARG JVM_OPTIONS
#
## Set maintainer
#LABEL maintainer="${MAINTAINER_EMAIL}"
#
## Set environment variables
#ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME} \
#    SPRING_CLOUD_CONSUL_HOST=${SPRING_CLOUD_CONSUL_HOST} \
#    SPRING_CLOUD_CONSUL_PORT=${SPRING_CLOUD_CONSUL_PORT} \
#    APP_PATH=/etc/conf/gaian/${SPRING_APPLICATION_NAME} \
#    APPLICATION_PKG=com.gaian \
#    OTEL_PROPAGATORS="b3multi" \
#    OTEL_SERVICE_NAME="${SPRING_APPLICATION_NAME}-trace" \
#    OTEL_TRACES_EXPORTER="zipkin" \
#    OTEL_JAVAAGENT_DEBUG="false" \
#    OTEL_EXPORTER_ZIPKIN_ENDPOINT="http://zipkin.istio-system.svc.cluster.local:9411/api/v2/spans" \
#    OTEL_INSTRUMENTATION_HTTP_CAPTURE_REQUEST_HEADERS="x-b3-traceid" \
#    JVM_OPTIONS=${JVM_OPTIONS}
#
## Copy the build jar file from builder (State 1)
#COPY --from=builder /build/target/${SPRING_APPLICATION_NAME}.jar /${SPRING_APPLICATION_NAME}.jar
#COPY --from=builder /build/criu.sh /criu.sh
#
## Expose the application port
#EXPOSE 8080
#
#
## Start the Java application
#ENTRYPOINT ["sh", "-c", "java $JVM_OPTIONS -jar ${SPRING_APPLICATION_NAME}.jar"]


# # Stage 1: Build the application
# FROM --platform=linux/amd64 gaianmobius/openjdk-21-mvn-3.9.6 as builder

# # Update Nexus Repository details to the correct URL
# ARG SPRING_APPLICATION_NAME
# ARG NEXUS_URL
# ARG NEXUS_USERNAME
# ARG NEXUS_PASSWORD
# ARG SONAR_HOST
# ARG SONAR_TOKEN
# ARG DEPLOYMENT_PLATFORM
# ARG SONAR_SCAN

# # Prepare Maven settings.xml with the correct Nexus URL and Central repository (Public repository)
# RUN mkdir -p /root/.m2 \
#  && echo "<?xml version='1.0' encoding='UTF-8'?> \
# <settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
#       xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
#       xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
#                           https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
#     <servers> \
#       <server> \
#         <id>nexus</id> \
#         <username>${NEXUS_USERNAME}</username> \
#         <password>${NEXUS_PASSWORD}</password> \
#       </server> \
#     </servers> \
#     <mirrors> \
#         <mirror> \
#             <id>nexus</id> \
#             <name>nexus</name>\
#             <mirrorOf>nexus</mirrorOf> \
#             <url>${NEXUS_URL}</url> \
#             <blocked>false</blocked>\
#         </mirror> \
#     </mirrors> \
# </settings>" > /root/.m2/settings.xml

# # Copy the .m2 repository from runner Cache(Git Workflow) to Docker
# COPY build/dependency/repository /root/.m2/repository


# # Copy the project files from Repositery to the container
# COPY pom.xml /build/pom.xml
# COPY src /build/src


# # Change container working directory
# WORKDIR /build


# # Conditional build steps based on DEPLOYMENT_PLATFORM
# RUN if [ "$SONAR_SCAN" = false ]; then \
#         mvn clean install -DskipTests -f pom.xml; \
#     elif [ "$SONAR_SCAN" = true ]; then \
#         mvn clean install -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SPRING_APPLICATION_NAME} -Dsonar.projectName=${SPRING_APPLICATION_NAME} -Dsonar.host.url=${SONAR_HOST} -Dsonar.login=${SONAR_TOKEN} -Dsonar.qualitygate.wait=true -f pom.xml; \
#     fi    
    
# # Renaming JAR File for Spring Application
# RUN for file in /build/target/${SPRING_APPLICATION_NAME}*.jar; do \
#     if [ -e "$file" ] && [ "$file" != "/build/target/${SPRING_APPLICATION_NAME}.jar" ]; then \
#         mv "$file" /build/target/${SPRING_APPLICATION_NAME}.jar && break; \
#     fi; \
#     done

# # Removing the Project data from container
# RUN rm -rf /root/.m2/settings.xml
# RUN rm -rf /build/pom.xml
# RUN rm -rf /build/src


# # Stage 2: Setup the runtime environment

# FROM eclipse-temurin:21-jre-alpine

# # Install jmap and other debug tools
# RUN apk update && apk add --no-cache openjdk21 && apk add --no-cache curl && rm -rf /var/cache/apk/*


# # Environment variables for the application
# ARG SPRING_APPLICATION_NAME
# ARG SPRING_CLOUD_CONSUL_HOST
# ARG SPRING_CLOUD_CONSUL_PORT
# ARG CONSUL_ENVIRONMENT
# ARG MAINTAINER_EMAIL
# ARG JVM_OPTIONS

# # Set maintainer
# LABEL maintainer="${MAINTAINER_EMAIL}"

# # Set environment variables
# ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME} \
#     SPRING_CLOUD_CONSUL_HOST=${SPRING_CLOUD_CONSUL_HOST} \
#     SPRING_CLOUD_CONSUL_PORT=${SPRING_CLOUD_CONSUL_PORT} \
#     APP_PATH=/etc/conf/gaian/${SPRING_APPLICATION_NAME} \
#     APPLICATION_PKG=com.gaian \
#     OTEL_PROPAGATORS="b3multi" \
#     OTEL_SERVICE_NAME="${SPRING_APPLICATION_NAME}-trace" \
#     OTEL_TRACES_EXPORTER="zipkin" \
#     OTEL_JAVAAGENT_DEBUG="false" \
#     OTEL_EXPORTER_ZIPKIN_ENDPOINT="http://zipkin.istio-system.svc.cluster.local:9411/api/v2/spans" \
#     OTEL_INSTRUMENTATION_HTTP_CAPTURE_REQUEST_HEADERS="x-b3-traceid" \
#     JVM_OPTIONS=${JVM_OPTIONS}

# # Copy the build jar file from builder (State 1)
# COPY --from=builder /build/target/${SPRING_APPLICATION_NAME}.jar /${SPRING_APPLICATION_NAME}.jar

# # Copy the src for Logback Configuration
# COPY src src

# # Copy the docker for OpenTelemetry jar
# COPY docker docker

# # Conditional Relocation of Logback Configuration
# RUN if [ -s src/main/resources/logback-spring.xml ]; then \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME} && \
#         mv src/main/resources/logback-spring.xml /etc/conf/gaian/${SPRING_APPLICATION_NAME}/logback-spring.xml; \
#     else \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME}; \
#     fi

# # Conditional Relocation of OpenTelemetry jar
# RUN if [ -s docker/opentelemetry-javaagent.jar ]; then \
#         mkdir -p /var/log/GC-Analyzer && \
#         touch /var/log/GC-Analyzer/gc.log && \
#         mv docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar; \
#     else \
#          echo "Skipping OpenTelemetry Java agent configuration - file not found"; \
#     fi
    
# # Removing the Project data from container
# RUN rm -rf src docker

# # Adding OpenTelemetry Jar
# ADD docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
# RUN mkdir -p /var/log/GC-Analyzer && touch /var/log/GC-Analyzer/gc.log

# # Expose the application port
# EXPOSE 8080


# # Start the Java application
# ENTRYPOINT ["sh", "-c", "java $JVM_OPTIONS -jar ${SPRING_APPLICATION_NAME}.jar"]


# #criu docker file

# #==============================================

# # Stage 1: Build the application
# FROM --platform=linux/amd64 gaianmobius/openjdk-21-mvn-3.9.6 as builder

# # Update Nexus Repository details to the correct URL
# ARG SPRING_APPLICATION_NAME
# ARG NEXUS_URL
# ARG NEXUS_USERNAME
# ARG NEXUS_PASSWORD
# ARG SONAR_HOST
# ARG SONAR_TOKEN
# ARG DEPLOYMENT_PLATFORM
# ARG SONAR_SCAN

# # Prepare Maven settings.xml with the correct Nexus URL and Central repository (Public repository)
# RUN mkdir -p /root/.m2 \
#  && echo "<?xml version='1.0' encoding='UTF-8'?> \
# <settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
#       xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
#       xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
#                           https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
#     <servers> \
#       <server> \
#         <id>nexus</id> \
#         <username>${NEXUS_USERNAME}</username> \
#         <password>${NEXUS_PASSWORD}</password> \
#       </server> \
#     </servers> \
#     <mirrors> \
#         <mirror> \
#             <id>nexus</id> \
#             <name>nexus</name>\
#             <mirrorOf>nexus</mirrorOf> \
#             <url>${NEXUS_URL}</url> \
#             <blocked>false</blocked>\
#         </mirror> \
#     </mirrors> \
# </settings>" > /root/.m2/settings.xml

# # Copy the .m2 repository from runner Cache(Git Workflow) to Docker
# COPY build/dependency/repository /root/.m2/repository


# # Copy the project files from Repositery to the container
# COPY pom.xml /build/pom.xml
# COPY src /build/src
# COPY docker/criu.sh /build/criu.sh
# RUN chmod +x /build/criu.sh


# # Change container working directory
# WORKDIR /build


# # Conditional build steps based on DEPLOYMENT_PLATFORM
# RUN if [ "$SONAR_SCAN" = false ]; then \
#         mvn clean install -DskipTests -f pom.xml; \
#     elif [ "$SONAR_SCAN" = true ]; then \
#         mvn clean install -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SPRING_APPLICATION_NAME} -Dsonar.projectName=${SPRING_APPLICATION_NAME} -Dsonar.host.url=${SONAR_HOST} -Dsonar.login=${SONAR_TOKEN} -Dsonar.qualitygate.wait=true -f pom.xml; \
#     fi    
    
# # Renaming JAR File for Spring Application
# RUN for file in /build/target/${SPRING_APPLICATION_NAME}*.jar; do \
#     if [ -e "$file" ] && [ "$file" != "/build/target/${SPRING_APPLICATION_NAME}.jar" ]; then \
#         mv "$file" /build/target/${SPRING_APPLICATION_NAME}.jar && break; \
#     fi; \
#     done

# # Removing the Project data from container
# RUN rm -rf /root/.m2/settings.xml
# RUN rm -rf /build/pom.xml
# RUN rm -rf /build/src


# # Stage 2: Setup the runtime environment
# FROM --platform=linux/amd64 gaianmobius/criu:v3.19

# # # Install jmap and other debug tools
# # RUN apk update && apk add --no-cache openjdk21 && apk add --no-cache curl && rm -rf /var/cache/apk/*


# # Environment variables for the application
# ARG SPRING_APPLICATION_NAME
# ARG SPRING_CLOUD_CONSUL_HOST
# ARG SPRING_CLOUD_CONSUL_PORT
# ARG CONSUL_ENVIRONMENT
# ARG MAINTAINER_EMAIL
# ARG JVM_OPTIONS

# # Set maintainer
# LABEL maintainer="${MAINTAINER_EMAIL}"

# # Set environment variables
# ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME} \
#     SPRING_CLOUD_CONSUL_HOST=${SPRING_CLOUD_CONSUL_HOST} \
#     SPRING_CLOUD_CONSUL_PORT=${SPRING_CLOUD_CONSUL_PORT} \
#     APP_PATH=/etc/conf/gaian/${SPRING_APPLICATION_NAME} \
#     APPLICATION_PKG=com.gaian \
#     OTEL_PROPAGATORS="b3multi" \
#     OTEL_SERVICE_NAME="${SPRING_APPLICATION_NAME}-trace" \
#     OTEL_TRACES_EXPORTER="zipkin" \
#     OTEL_JAVAAGENT_DEBUG="false" \
#     OTEL_EXPORTER_ZIPKIN_ENDPOINT="http://zipkin.istio-system.svc.cluster.local:9411/api/v2/spans" \
#     OTEL_INSTRUMENTATION_HTTP_CAPTURE_REQUEST_HEADERS="x-b3-traceid" \
#     JVM_OPTIONS=${JVM_OPTIONS}

# # Copy the build jar file from builder (State 1)
# COPY --from=builder /build/target/${SPRING_APPLICATION_NAME}.jar /${SPRING_APPLICATION_NAME}.jar

# COPY --from=builder /build/criu.sh /criu.sh

# # Copy the src for Logback Configuration
# COPY src src

# # Copy the docker for OpenTelemetry jar
# COPY docker docker

# # Conditional Relocation of Logback Configuration
# RUN if [ -s src/main/resources/logback-spring.xml ]; then \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME} && \
#         mv src/main/resources/logback-spring.xml /etc/conf/gaian/${SPRING_APPLICATION_NAME}/logback-spring.xml; \
#     else \
#         mkdir -p /etc/conf/gaian/${SPRING_APPLICATION_NAME}; \
#     fi

# # Conditional Relocation of OpenTelemetry jar
# RUN if [ -s docker/opentelemetry-javaagent.jar ]; then \
#         mkdir -p /var/log/GC-Analyzer && \
#         touch /var/log/GC-Analyzer/gc.log && \
#         mv docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar; \
#     else \
#          echo "Skipping OpenTelemetry Java agent configuration - file not found"; \
#     fi
    
# # Removing the Project data from container
# RUN rm -rf src docker

# # Adding OpenTelemetry Jar
# ADD docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
# RUN mkdir -p /var/log/GC-Analyzer && touch /var/log/GC-Analyzer/gc.log

# # Expose the application port
# EXPOSE 8080


# # Start the Java application
# #ENTRYPOINT ["sleep 3600"]
# #ENTRYPOINT ["/opt/zulu21.34.19-ca-crac-jdk21.0.3-linux_x64/bin/java", "-jar", "bob-camunda.jar", "&"]
# ENTRYPOINT ["./criu.sh"]
