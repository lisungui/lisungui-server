#!/bin/sh

# This script writes the service account JSON content to the app.yaml file in a specific format.

# Check if the FIRESTORE_APPLICATION_CREDENTIALS_J environment variable is set
if [ -z "$FIRESTORE_APPLICATION_CREDENTIALS_J" ]; then
  echo "ERROR: FIRESTORE_APPLICATION_CREDENTIALS_J environment variable is not set."
  exit 1
fi

# Validate and format the FIRESTORE_APPLICATION_CREDENTIALS_J to ensure it is in JSON format
if ! echo "$FIRESTORE_APPLICATION_CREDENTIALS_J" | grep -q '^{.*}$'; then
  echo "WARNING: FIRESTORE_APPLICATION_CREDENTIALS_J environment variable is not in valid JSON format. Formatting it now."
  FIRESTORE_APPLICATION_CREDENTIALS_J="{\"key\":\"${FIRESTORE_APPLICATION_CREDENTIALS_J}\"}"
  echo "Formatted FIRESTORE_APPLICATION_CREDENTIALS_J: $FIRESTORE_APPLICATION_CREDENTIALS_J"
fi

# Path to the app.yaml file
APP_YAML_PATH="./app.yaml"

# Check if the app.yaml file exists
if [ ! -f "$APP_YAML_PATH" ]; then
  echo "Creating app.yaml because it does not exist."
  touch "$APP_YAML_PATH"
fi

# Escape quotes and handle new lines in JSON string for YAML compatibility
SERVICE_ACCOUNT_JSON=$(echo "$FIRESTORE_APPLICATION_CREDENTIALS_J" | sed 's/"/\\"/g' | sed ':a;N;$!ba;s/\n/\\n    /g')

# Prepare the YAML content to be written
YAML_CONTENT="  FIRESTORE_APPLICATION_CREDENTIALS_J: |\n    $SERVICE_ACCOUNT_JSON"

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

