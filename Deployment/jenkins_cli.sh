if [[ ! -e jenkins-cli.jar ]]; then
  echo ""
  echo "Download the jenkins CLI tool first."
  echo ""
  exit 1
fi

NODE_IP=$(./node_ip.sh)
CREDENTIALS=$(cat jenkins_creds.txt)
# shellcheck disable=SC2068
java -jar jenkins-cli.jar -s "http://${NODE_IP}:8080" -auth "${CREDENTIALS}" $@
