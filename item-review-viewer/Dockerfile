FROM tomcat:7-jre7

ARG contentStore=/home/tomcat7/content/
ARG contextName=ROOT

RUN mkdir -p $contentStore
RUN mkdir -p $contentStore/gitlab
RUN mkdir -p $contentStore/gitlab_archive
COPY ./ItemsPatch.xml $contentStore/gitlab




WORKDIR /usr/local/tomcat/

COPY ./ItemsPatch.xml ${contentStore}
COPY context.xml ./conf
RUN  rm -rf webapps/*

COPY ./target/item-review-viewer.war ./webapps
RUN mv ./webapps/item-review-viewer.war ./webapps/$contextName.war

EXPOSE 8080

ENTRYPOINT ["catalina.sh", "run"]