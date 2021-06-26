NODE_IP=$(./node_ip.sh)
ssh -v -i ec2_key.pem "ubuntu@${NODE_IP}"
