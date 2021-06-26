NODE_IP=$(./node_ip.sh)
curl "http://${NODE_IP}:8080/jnlpJars/jenkins-cli.jar" --output jenkins-cli.jar
