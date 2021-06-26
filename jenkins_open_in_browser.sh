if [[ ! -e jenkins-cli.jar ]]; then
  echo ""
  echo "Download the jenkins CLI tool first."
  echo ""
  exit 1
fi

NODE_IP=$(./node_ip.sh)
open "http://${NODE_IP}:8080"