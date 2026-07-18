#!/usr/bin/env bash
set -euo pipefail

docker compose up --build -d

echo "Local stack is starting..."
echo "Marketplace site: http://localhost:8080/"
echo "API (via nginx):  http://localhost:8080/api/v1/health"
