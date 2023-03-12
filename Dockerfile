FROM adoptopenjdk/openjdk11:alpine-jre
COPY entryScript.sh /entryScript.sh
COPY build/libs/recipes-0.0.1-SNAPSHOT.jar /opt/app.jar
EXPOSE 8080
RUN ["chmod", "+x", "/entryScript.sh"]
ENTRYPOINT ["/entryScript.sh"]
