#!/bin/sh

# This script writes the API key to the app.yaml file in a specific format.

# Check if the FIRESTORE_APPLICATION_CREDENTIALS_J environment variable is set
if [ -z "$FIRESTORE_APPLICATION_CREDENTIALS_J" ]; then
  echo "ERROR: FIRESTORE_APPLICATION_CREDENTIALS_J environment variable is not set."
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
ENV_VAR_CONTENT="  FIRESTORE_APPLICATION_CREDENTIALS_J: \"$FIRESTORE_APPLICATION_CREDENTIALS_J\""

# Write the content to the app.yaml, ensuring proper formatting
if grep -q "env_variables:" "$APP_YAML_PATH"; then
  echo "Updating existing env_variables section."
  if grep -q "FIRESTORE_APPLICATION_CREDENTIALS_J:" "$APP_YAML_PATH"; then
    echo "Replacing existing FIRESTORE_APPLICATION_CREDENTIALS_J entry."
    sed -i "s|FIRESTORE_APPLICATION_CREDENTIALS_J:.*|$ENV_VAR_CONTENT|" "$APP_YAML_PATH"
  else
    echo "Adding new FIRESTORE_APPLICATION_CREDENTIALS_J entry to existing env_variables section."
    sed -i "/env_variables:/a\\
$ENV_VAR_CONTENT" "$APP_YAML_PATH"
  fi
else
  echo "Adding new env_variables section."
  echo -e "\nenv_variables:\n  ENV: 'prod'\n$ENV_VAR_CONTENT" >> "$APP_YAML_PATH"
fi

echo "API key has been written to $APP_YAML_PATH"
