FROM maven
WORKDIR /usr/src/app
ADD . .
RUN mvn package -Dmaven.test.skip=true
EXPOSE 8080

ENTRYPOINT ["mvn", "exec:java"]
