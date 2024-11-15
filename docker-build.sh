#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

BITCOIN_CLIENT_APP_NAME="bitcoin-client"
BITCOIN_API_APP_NAME="bitcoin-api"

BITCOIN_CLIENT_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${BITCOIN_CLIENT_APP_NAME}:${APP_VERSION}"
BITCOIN_API_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${BITCOIN_API_APP_NAME}:${APP_VERSION}"

SKIP_TESTS="true"

./mvnw clean spring-boot:build-image --projects "$BITCOIN_CLIENT_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$BITCOIN_CLIENT_DOCKER_IMAGE_NAME"
./mvnw clean spring-boot:build-image --projects "$BITCOIN_API_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$BITCOIN_API_DOCKER_IMAGE_NAME"
