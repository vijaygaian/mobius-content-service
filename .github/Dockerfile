FROM gaianmobius/openjdk:21-jdk

MAINTAINER durga.prasad@gaiansolutions.com

#ADD docker/confd /etc/confd
ADD docker/startup.sh /

RUN mkdir -p /etc/conf/gaian/mobius-content-service
RUN mkdir -p /var/lib/gaian/mobius-content-service
RUN mkdir -p /var/log/gaian/mobius-content-service

# RUN apk add lftp

# # Install Tini
# ENV TINI_VERSION v0.19.0
# ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini-static /tini
# RUN chmod +x /tini

# RUN apk add --no-cache tzdata
# ENV TZ=Asia/Kolkata

EXPOSE 8080

# run the application
#CMD ["sh"]
ENTRYPOINT ["sh", "/startup.sh"]

ADD docker/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

# add the jar file
ADD target/mobius-content-service.jar mobius-content-service.jar
