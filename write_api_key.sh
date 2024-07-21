#!/bin/sh

# This script writes the API key to the app.yaml file in a specific format.

# Check if the API_KEY environment variable is set
if [ -z "$FIRESTORE_APPLICATION_CREDENTIALS_JSON" ]; then
  echo "ERROR: FIRESTORE_APPLICATION_CREDENTIALS_JSON environment variable is not set."
  exit 1
fi

# Path to the app.yaml file
APP_YAML_PATH="./app.yaml"

# Check if the app.yaml file exists
if [ ! -f "$APP_YAML_PATH" ]; then
  echo "Creating app.yaml because it does not exist."
  touch "$APP_YAML_PATH"
fi

# Prepare the content to be written
CONTENT="env_variables:\n  ENV: 'prod'\n  FIRESTORE_APPLICATION_CREDENTIALS_JSON: '$FIRESTORE_APPLICATION_CREDENTIALS_JSON'"

# Write the content to the app.yaml
echo "$CONTENT" >> $APP_YAML_PATH

echo "API key has been written to $APP_YAML_PATH"
