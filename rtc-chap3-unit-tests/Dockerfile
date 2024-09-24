FROM amazoncorretto:21
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/rtc-chap3-unit-tests-0.0.1-SNAPSHOT.jar rtcchap3persistence.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar rtcchap3persistence.jar
