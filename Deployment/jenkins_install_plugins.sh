if [[ ! -e jenkins-cli.jar ]]
then
  echo ""
  echo "Download the jenkins CLI tool first."
  echo ""
  exit 1
fi

JENKINS_IP=$(./jenkins_ip.sh)
CREDENTIALS=$(cat jenkins_creds.txt)

echo "You can view the progress of these installations at http://${JENKINS_IP}:8080/updateCenter/"

function install_plugin {
  ./jenkins_cli.sh install-plugin $1 -deploy
}

install_plugin cloudbees-folder
install_plugin antisamy-markup-formatter
install_plugin build-timeout
install_plugin credentials-binding
install_plugin ws-cleanup
install_plugin ant
install_plugin okhttp-api
install_plugin parameterized-trigger
install_plugin github-branch-source
install_plugin git-parameter
install_plugin email-ext
install_plugin mailer
install_plugin htmlpublisher
install_plugin timestamper
install_plugin workflow-cps-global-lib
install_plugin pipeline-model-definition
install_plugin pipeline-utility-steps
install_plugin pipeline-github-lib
install_plugin pipeline-stage-view