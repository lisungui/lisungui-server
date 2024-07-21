#!/bin/sh

# This script writes the service account JSON content to the app.yaml file in a specific format.

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

# Handle JSON string as a literal block scalar in YAML
# Adding indentation to each line of JSON for proper YAML formatting
SERVICE_ACCOUNT_JSON=$(echo "$FIRESTORE_APPLICATION_CREDENTIALS_J" | sed 's/"/\\"/g' | sed 's/^/    /')

# Prepare the YAML content to be written
YAML_CONTENT="  FIRESTORE_APPLICATION_CREDENTIALS_J: |\n$SERVICE_ACCOUNT_JSON"

# Write the content to the app.yaml, ensuring proper formatting
if grep -q "env_variables:" "$APP_YAML_PATH"; then
  echo "Updating existing env_variables section."
  # Append after the line containing 'env_variables:'
  sed -i "/env_variables:/a\\
$YAML_CONTENT" "$APP_YAML_PATH"
else
  echo "Adding new env_variables section."
  echo -e "\nenv_variables:\n$YAML_CONTENT" >> "$APP_YAML_PATH"
fi

echo "Service account JSON has been written to $APP_YAML_PATH"
