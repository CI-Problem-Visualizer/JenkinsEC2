JENKINS_IP=$(./jenkins_ip.sh)
curl http://${JENKINS_IP}:8080/jnlpJars/jenkins-cli.jar --output jenkins-cli.jar