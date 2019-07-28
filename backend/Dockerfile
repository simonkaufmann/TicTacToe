FROM maven as build
WORKDIR project
COPY . .
RUN mvn package

FROM openjdk
WORKDIR app
COPY --from=build /project/target/tic_tac_toe.jar .
CMD java -jar tic_tac_toe.jar ${PORT}
