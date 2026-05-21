FROM tomcat:9.0-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY dist/QuanLyThuVien.war /usr/local/tomcat/webapps/ROOT.war

ENV CATALINA_OPTS="-Dfile.encoding=UTF-8"
EXPOSE 8080

CMD ["catalina.sh", "run"]
