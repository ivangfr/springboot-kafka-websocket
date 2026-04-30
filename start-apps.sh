#!/usr/bin/env bash
set -e

source scripts/my-functions.sh

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

BITCOIN_API_APP_NAME="bitcoin-api"
BITCOIN_CLIENT_APP_NAME="bitcoin-client"

BITCOIN_API_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${BITCOIN_API_APP_NAME}:${APP_VERSION}"
BITCOIN_CLIENT_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${BITCOIN_CLIENT_APP_NAME}:${APP_VERSION}"

echo
echo "Starting bitcoin-api..."

docker run -d --rm --name bitcoin-api -p 9081:8080 \
  -e MYSQL_HOST=mysql -e KAFKA_HOST=kafka -e KAFKA_PORT=9092 \
  --network=springboot-kafka-websocket_default \
  "$BITCOIN_API_DOCKER_IMAGE_NAME"

echo
echo "Starting bitcoin-client..."

docker run -d --rm --name bitcoin-client -p 9082:8080 \
  -e KAFKA_HOST=kafka -e KAFKA_PORT=9092 \
  --network=springboot-kafka-websocket_default \
  "$BITCOIN_CLIENT_DOCKER_IMAGE_NAME"

echo
wait_for_container_log "bitcoin-client" "Started"

echo
wait_for_container_log "bitcoin-api" "Started"

printf "\n"
printf "%15s | %37s |\n" "Application" "URL"
printf "%15s + %37s |\n" "--------------" "-------------------------------------"
printf "%15s | %37s |\n" "bitcoin-api" "http://localhost:9081/swagger-ui.html"
printf "%15s | %37s |\n" "bitcoin-client" "http://localhost:9082"
printf "\n"
