NODE_IP=$(./node_ip.sh)
ssh -v -i jenkins_server_keys.pem "ubuntu@${NODE_IP}"
