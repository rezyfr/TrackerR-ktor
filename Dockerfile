FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/trackerr.jar /app/trackerr.jar
ENTRYPOINT ["java","-jar","/app/trackerr.jar"]