#!/bin/bash

# Function to start WireMock if not running
start_wiremock() {
  local host="localhost"
  local port=$1
  local log_file=$2

  WIREMOCK_RUNNING=$(ps aux | grep "wiremock-standalone.*--port $port" | grep -v grep)

  if [ -z "$WIREMOCK_RUNNING" ]; then
    echo "WireMock on port $port is not running. Starting WireMock..."
    java -jar ./wiremock1/wiremock-standalone-2.14.0.jar --port $port --verbose &> $log_file 2>&1 &
    echo "WireMock started on host $host and port $port. PID: $!"
  else
    echo "WireMock on port $port is already running."
  fi
}

# Function to start PostgreSQL container if not running
start_postgres() {
  POSTGRES_RUNNING=$(docker ps | grep checkout_container)
  POSTGRES_CONTAINER_EXISTS=$(docker ps -a | grep checkout_container)

  if [ -z "$POSTGRES_RUNNING" ]; then
    if [ -z "$POSTGRES_CONTAINER_EXISTS" ]; then
      echo "PostgreSQL container does not exist. Creating and starting container..."
      docker-compose up -d
    else
      echo "PostgreSQL is not running. Starting Docker container for PostgreSQL..."
      docker start checkout_container
    fi
  else
    echo "PostgreSQL is already running."
  fi
}

# Start PostgreSQL container
start_postgres

# Start WireMock instances
start_wiremock 8082 ./wiremock/wiremock.log
start_wiremock 8083 ./wiremock2/wiremock2.log

