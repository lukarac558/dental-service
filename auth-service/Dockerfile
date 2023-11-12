FROM quay.io/keycloak/keycloak:22.0.4

COPY ./auth-service/import /opt/keycloak/data/import

COPY ./cert/auth-service.crt.pem /opt/keycloak/conf/auth-service.crt.pem
COPY ./cert/auth-service.key.pem /opt/keycloak/conf/auth-service.key.pem

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", "--features=preview"]