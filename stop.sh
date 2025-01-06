#!/bin/bash

CONFIG_FILE="wiremock_config.json"
grep -o '"port":[^,]*' "$CONFIG_FILE" | while read -r line; do
  port=$(echo $line | cut -d ':' -f 2 | tr -d ' "')
  echo "Stopping WireMock on port $port..."
  curl -X POST "http://localhost:$port/__admin/shutdown"
done

# Check if the Docker container for PostgreSQL is running
POSTGRES_RUNNING=$(docker ps | grep checkout_container)

# If the Docker container for PostgreSQL is running, stop it
if [ -n "$POSTGRES_RUNNING" ]; then
  echo "Stopping Docker container for PostgreSQL..."
  docker stop checkout_container
else
  echo "PostgreSQL container is not running."
fi