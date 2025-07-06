#!/bin/bash

# Define service-to-fragments mapping
declare -A serviceFragments
serviceFragments["api-gateway"]="eureka.properties"
serviceFragments["media-service"]="eureka.properties"
serviceFragments["storeVideos"]="eureka.properties mysql.properties rabbit-mq.properties"
serviceFragments["stream-service"]="eureka.properties mysql.properties"
serviceFragments["uploadVideos"]="eureka.properties redis.properties"
serviceFragments["naming-server"]=""
# Clear previous outputs
rm -f api-gateway.properties media-service.properties naming-server.properties storeVideos.properties stream-service.properties uploadVideos.properties

# Generate each final service config
for service in "${!serviceFragments[@]}"; do
  echo "Generating config for $service..."
  > ${service}.properties

  for fragment in ${serviceFragments[$service]}; do
    cat shared-config/$fragment >> ${service}.properties
    echo "" >> ${service}.properties
  done

  # Append service-specific manual config
  if [ -f "service-overrides/${service}.properties" ]; then
    echo "# Service-specific overrides" >> ${service}.properties
    cat service-overrides/${service}.properties >> ${service}.properties
  fi
done

echo "✅ Config generation complete."
