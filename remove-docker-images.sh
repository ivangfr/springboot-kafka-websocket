#!/usr/bin/env bash
set -e

DOCKER_IMAGE_PREFIX="ivanfranchin"
BITCOIN_API_APP_NAME="bitcoin-api"
BITCOIN_CLIENT_APP_NAME="bitcoin-client"
APP_VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)

docker rmi "${DOCKER_IMAGE_PREFIX}/${BITCOIN_API_APP_NAME}:${APP_VERSION}" 2>/dev/null || true
docker rmi "${DOCKER_IMAGE_PREFIX}/${BITCOIN_CLIENT_APP_NAME}:${APP_VERSION}" 2>/dev/null || true
