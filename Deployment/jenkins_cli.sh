if [[ ! -e jenkins-cli.jar ]]
then
  echo ""
  echo "Download the jenkins CLI tool first."
  echo ""
  exit 1
fi

JENKINS_IP=$(./jenkins_ip.sh)
CREDENTIALS=$(cat jenkins_creds.txt)
java -jar jenkins-cli.jar -s http://${JENKINS_IP}:8080 -auth ${CREDENTIALS} $@