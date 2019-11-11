#!/usr/bin/env bash

./mvnw clean package dockerfile:build -DskipTests --projects bitcoin-client
./mvnw clean package dockerfile:build -DskipTests --projects bitcoin-api
