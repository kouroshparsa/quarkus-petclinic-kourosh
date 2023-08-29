FROM registry.access.redhat.com/ubi8/openjdk-17:1.16


USER root
WORKDIR /build

RUN ls /build
COPY pom.xml /build
COPY src /build/src


RUN mvn dependency:go-offline

RUN mvn package -Dmaven.test.skip=true

RUN grep version /build/target/maven-archiver/pom.properties | cut -d '=' -f2 >.env-version
RUN grep artifactId /build/target/maven-archiver/pom.properties | cut -d '=' -f2 >.env-id

# if this is an uber jar create a structure that looks the same as fast-jar with empty directories
# this allows for the same dockerfile to be used with both
RUN if [ ! -d /build/target/quarkus-app ] ; then mkdir -p /build/target/quarkus-app/lib; \
     mkdir -p /build/target/quarkus-app/app; \
     mkdir -p /build/target/quarkus-app/quarkus; \
     mv /build/target/$(cat .env-id)-$(cat .env-version)*.jar /build/target/quarkus-app/ ; \
     fi

FROM registry.access.redhat.com/ubi8/openjdk-17-runtime:1.15-1.1682053056
# Configure the JAVA_OPTS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=8080 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --from=0 --chown=1001 /build/target/quarkus-app/lib/ /deployments/lib/
COPY --from=0 --chown=1001 /build/target/quarkus-app/*.jar /deployments/export-run-artifact.jar
COPY --from=0 --chown=1001 /build/target/quarkus-app/app/ /deployments/app/
COPY --from=0 --chown=1001 /build/target/quarkus-app/quarkus/ /deployments/quarkus/
EXPOSE 8080
#ENTRYPOINT ["/opt/jboss/container/java/run/run-java.sh"]
CMD ["./mvnw", "package", "-DskipTests", "-Dquarkus.container-image.push=true"]
