FROM swaggerapi/swagger-ui:latest
MAINTAINER DMITRY

ADD /user-service/spec/user-service.yml /usr/share/nginx/html/doc/user-service.yaml
ADD /audit-service/spec/audit-service.yml /usr/share/nginx/html/doc/audit-service.yaml
ADD /report-service/spec/report-service.yml /usr/share/nginx/html/doc/report-service.yaml
ADD /flats-service/spec/flats-service.yml /usr/share/nginx/html/doc/flats-service.yaml

ENV URLS="[{ url: 'doc/user-service.yaml', name: 'User'},{ url: 'doc/audit-service.yaml', name: 'Audit'},{ url: 'doc/report-service.yaml', name: 'Report'},{ url: 'doc/flats-service.yaml', name: 'Flats'},]"
ENV URLS_PRIMARY_NAME="Flats"