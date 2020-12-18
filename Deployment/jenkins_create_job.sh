if [[ ! -e jenkins-cli.jar ]]
then
  echo ""
  echo "Download the jenkins CLI tool first."
  echo ""
  exit 1
fi

if [[ -z $1 ]]
then
  echo ""
  echo "Pass in the filename for the XML job specification"
  echo ""
  exit 1
fi

./jenkins_cli.sh create-job CodeSpyGlass < "$1"