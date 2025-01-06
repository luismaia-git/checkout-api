#!/bin/bash

# Start WireMock instances
start_wiremock() {
  local host="localhost"
  local port=$1
  local log_file=$2
  local root_dir=$3
  local wiremock_jar_path="$root_dir/wiremock-standalone-2.14.0.jar"

  if [ ! -f "$wiremock_jar_path" ]; then
    echo "WireMock JAR not found at $wiremock_jar_path"
    return 1
  fi

  WIREMOCK_RUNNING=$(ps aux | grep "wiremock-standalone.*--port $port" | grep -v grep)

  if [ -z "$WIREMOCK_RUNNING" ]; then
    echo "WireMock on port $port is not running. Starting WireMock..."
    java -jar "$wiremock_jar_path" --port $port --root-dir "$root_dir" --verbose &> "$log_file" 2>&1 &

    if [ $? -eq 0 ]; then
      echo "WireMock started on host $host and port $port. PID: $!"
    else
      echo "Failed to start WireMock on port $port."
    fi
  else
    echo "WireMock on port $port is already running."
  fi
}

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

CONFIG_FILE="wiremock_config.json"
cat $CONFIG_FILE | jq -c '.[]' | while read -r instance; do
  port=$(echo $instance | jq -r '.port')
  log_file=$(echo $instance | jq -r '.log_file')
  root_dir=$(echo $instance | jq -r '.root_dir')

  if [[ -n "$port" && -n "$log_file" && -n "$root_dir" ]]; then
    abs_root_dir=$(realpath "$root_dir")
    abs_log_file=$(realpath "$log_file")

    echo "Starting WireMock in directory $abs_root_dir on port $port with log_file $abs_log_file..."
    start_wiremock "$port" "$abs_log_file" "$abs_root_dir"
  else
    echo "Invalid configuration: Missing port, log_file, or root_dir"
  fi
done
