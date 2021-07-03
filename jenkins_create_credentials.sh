#!/usr/bin/env bash

if [[ ! -e jenkins-cli.jar ]]; then
  echo ""
  echo "Download the jenkins CLI tool first."
  echo ""
  exit 1
fi

if [[ $# != "4" ]]; then
  echo "USAGE:   $0 <id> <description> <credentialUsername> <credentialPassword>"
  echo "EXAMPLE: ./jenkins_create_credentials.sh GitHubPushAccess \"Repo push access\" \"robmoore-i\" \"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\""
  exit 1
fi

ID=$1
DESCRIPTION=$2
USERNAME=$3
PASSWORD=$4

CREDENTIALS_XML="<com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl plugin=\"credentials@2.5\">
  <scope>GLOBAL</scope>
  <id>$ID</id>
  <description>$DESCRIPTION</description>
  <username>$USERNAME</username>
  <password>$PASSWORD</password>
  <usernameSecret>true</usernameSecret>
</com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl>"

echo "$CREDENTIALS_XML" | ./jenkins_cli.sh create-credentials-by-xml "system::system::jenkins" "(global)"