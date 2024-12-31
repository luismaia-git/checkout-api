#!/bin/bash

# Check if WireMock is running
WIREMOCK_RUNNING=$(ps aux | grep wiremock-standalone | grep -v grep)

# Check if the Docker container for PostgreSQL is running
POSTGRES_RUNNING=$(docker ps | grep checkout_container)

# If WireMock is running, stop it
if [ -n "$WIREMOCK_RUNNING" ]; then
  echo "Stopping WireMock..."
  ./wiremock/stop.sh
else
  echo "WireMock is not running."
fi

# If the Docker container for PostgreSQL is running, stop it
if [ -n "$POSTGRES_RUNNING" ]; then
  echo "Stopping Docker container for PostgreSQL..."
  docker stop checkout_container
else
  echo "PostgreSQL container is not running."
fi