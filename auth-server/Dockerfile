FROM quay.io/keycloak/keycloak:22.0.4

COPY ./import /opt/keycloak/data/import

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", "--features=preview"]