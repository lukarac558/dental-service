#!/usr/bin/env bash

docker stop auth-server
docker stop db

docker container rm db || true
docker container rm auth-server || true
docker image rm custom-keycloak || true

docker volume rm dental-service_postgres_data || true