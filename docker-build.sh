#!/usr/bin/env bash

./mvnw clean compile jib:dockerBuild --projects bitcoin-client
./mvnw clean compile jib:dockerBuild --projects bitcoin-api
