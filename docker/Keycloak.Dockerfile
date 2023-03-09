FROM quay.io/keycloak/keycloak:20.0.2

WORKDIR /opt/keycloak

COPY themes themes
COPY realm-config data/import

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgresql://db:5432/keycloak_db
ENV KC_DB_USERNAME=keycloak
ENV KC_DB_PASSWORD=keycloak
ENV KC_HOSTNAME=localhost
ENV KC_DB_SCHEMA=public
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin
ENV KC_HTTP_PORT=9080
ENV KC_HTTPS_PORT=9443
ENV KC_HTTP_RELATIVE_PATH=keycloak

EXPOSE 9080
EXPOSE 9443

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev --import-realm"]