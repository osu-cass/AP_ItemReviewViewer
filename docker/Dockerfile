FROM tomcat:7-jre7

RUN mkdir -d /home/tomcat7/content

WORKDIR /usr/local/tomcat/

COPY context.xml ./conf
RUN  rm -rf webapps/*

COPY item-review-viewer.war ./webapps
RUN mv ./webapps/item-review-viewer.war ./webapps/$contextName.war

EXPOSE 8080
ENTRYPOINT ["catalina.sh", "run"]