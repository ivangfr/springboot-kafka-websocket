#!/usr/bin/env bash

echo
echo "Starting bitcoin-api..."

docker run -d --rm --name bitcoin-api -p 9081:8080 \
  -e MYSQL_HOST=mysql -e KAFKA_HOST=kafka -e KAFKA_PORT=9092 -e ZIPKIN_HOST=zipkin \
  --network=springboot-kafka-websocket_default \
  --health-cmd="curl -f http://localhost:8080/actuator/health || exit 1" \
  ivanfranchin/bitcoin-api:1.0.0

echo
echo "Starting bitcoin-client..."

docker run -d --rm --name bitcoin-client -p 9082:8080 \
  -e KAFKA_HOST=kafka -e KAFKA_PORT=9092 -e ZIPKIN_HOST=zipkin \
  --network=springboot-kafka-websocket_default \
  --health-cmd="curl -f http://localhost:8080/actuator/health || exit 1" \
  ivanfranchin/bitcoin-client:1.0.0

printf "\n"
printf "%15s | %37s |\n" "Application" "URL"
printf "%15s + %37s |\n" "--------------" "-------------------------------------"
printf "%15s | %37s |\n" "bitcoin-api" "http://localhost:9081/swagger-ui.html"
printf "%15s | %37s |\n" "bitcoin-client" "http://localhost:9082"
printf "\n"
