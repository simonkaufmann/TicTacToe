FROM maven
WORKDIR project
COPY . .
RUN maven build
