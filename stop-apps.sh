#!/usr/bin/env bash
set -e

docker stop bitcoin-api bitcoin-client 2>/dev/null || true
