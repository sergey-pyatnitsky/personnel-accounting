FROM tomcat:9.0-jdk8
WORKDIR /usr/app
COPY /web/target/web-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ENV CATALINA_OPTS="-Ddatasource.url=jdbc:mysql://host.docker.internal:3306/personnel_accounting -Ddatasource.username=root -Ddatasource.password=1234"
EXPOSE 8080
CMD ["catalina.sh", "run"]