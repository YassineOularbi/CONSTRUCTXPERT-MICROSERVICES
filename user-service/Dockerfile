FROM openjdk:22
VOLUME /tmp
COPY target/*.jar  app.jar
ENTRYPOINT ["java","--enable-preview","-jar", "app.jar"]