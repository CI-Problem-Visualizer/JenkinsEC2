if [[ ! -e jenkins-cli.jar ]]
then
  echo ""
  echo "Download the jenkins CLI tool first."
  echo ""
  exit 1
fi

JENKINS_IP=$(./jenkins_ip.sh)
CREDENTIALS=$(cat jenkins_creds.txt)
./jenkins_cli.sh get-job CodeEvaluation
