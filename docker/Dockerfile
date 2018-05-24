FROM tomcat:7-jre7

ARG source=content/
ARG contentStore=/home/tomcat7/content/
ARG contextName=ROOT

COPY $source $contentStore

WORKDIR /usr/local/tomcat/

COPY context.xml ./conf
RUN  rm -rf webapps/*

COPY item-review-viewer.war ./webapps
RUN mv ./webapps/item-review-viewer.war ./webapps/$contextName.war

ENTRYPOINT ["catalina.sh", "run"]