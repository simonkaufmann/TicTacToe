FROM node AS build
WORKDIR project
COPY . .
RUN npm install
RUN npm run build
COPY ./docker/htaccess build/.htaccess

FROM httpd
COPY --from=build /project/build /usr/local/apache2/htdocs
COPY ./docker/httpd.conf /usr/local/apache2/conf/httpd.conf
