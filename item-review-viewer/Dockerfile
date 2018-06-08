FROM tomcat:7-jre7

ARG source=content/
ARG contentStore=/home/tomcat7/$source
ARG contextName=ROOT

RUN mkdir -p $contentStore
RUN mkdir -p $contentStore/gitlab
RUN mkdir -p $contentStore/gitlab_archive

WORKDIR /usr/local/tomcat/

COPY context.xml ./conf
RUN  rm -rf webapps/*

COPY ./target/item-review-viewer.war ./webapps
RUN mv ./webapps/item-review-viewer.war ./webapps/$contextName.war

ENTRYPOINT ["catalina.sh", "run"]