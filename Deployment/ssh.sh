JENKINS_IP=$(./jenkins_ip.sh)
ssh -v -i jenkins_server_keys.pem ubuntu@${JENKINS_IP}
