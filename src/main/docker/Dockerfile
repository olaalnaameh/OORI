FROM frolvlad/alpine-oraclejdk8:slim

# Supported environment variables
# Port of spring boot app (overrides application.properties/server.port)
ENV SERVER_PORT 8080
ENV ENDPOINT_URL http://localhost:3030/odfData

ADD omi-odf-rdf-integration-0.0.1-SNAPSHOT.jar omi-odf-rdf-integration.jar
ADD run.sh run.sh

RUN sh -c 'touch /omi-odf-rdf-integration.jar'
RUN sh -c 'chmod +x /run.sh'

ENTRYPOINT ["/run.sh"]