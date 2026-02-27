FROM maven:latest as builder
ENV M2_HOME /usr/share/maven
WORKDIR /usr/work
COPY ./OpLoRServerPrototype /usr/work
RUN mvn package 

FROM tomcat:9.0.46
COPY --from=builder /usr/work/target/OpLoRServerPrototype-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/

#sqlite
ENV DB_FILE_BASE=test


RUN sed -i -e 's/deb.debian.org/archive.debian.org/g' -e 's|security.debian.org|archive.debian.org/|g' -e '/buster-updates/d' /etc/apt/sources.list

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get install -y sqlite3 
RUN mkdir /usr/local/tomcat/dbfile
RUN mkdir /usr/local/tomcat/db
COPY ./CreateLogTable.sql /usr/local/tomcat/dbfile
COPY ./entrypoint.sh /usr/local/tomcat/dbfile
ENTRYPOINT sh -x /usr/local/tomcat/dbfile/entrypoint.sh