FROM amazoncorretto:21
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/rtc-chap6-servlet-0.0.1-SNAPSHOT.jar rtcchap6servlet.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar rtcchap6servlet.jar
