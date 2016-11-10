FROM frolvlad/alpine-oraclejdk8:slim

# Supported environment variables
# Port of spring boot app (overrides application.properties/server.port)
ENV SERVER_PORT 8080

VOLUME /tmp

ADD data-dump-app-0.0.1-SNAPSHOT.jar ids-app.jar
ADD run.sh run.sh

RUN mkdir /tmp/datasets
RUN mkdir /tmp/metadata

RUN sh -c 'touch /ids-app.jar'
RUN sh -c 'chmod +x /run.sh'

ENTRYPOINT ["/run.sh"]