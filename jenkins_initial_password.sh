NODE_IP=$(./node_ip.sh)
ssh -i ec2_key.pem "ubuntu@${NODE_IP}" <<'EOF'
  CONTAINER_ID=$(sudo docker ps | grep "jenkins/jenkins:jdk11" | cut -d " " -f1)
  sudo docker exec ${CONTAINER_ID} cat /var/jenkins_home/secrets/initialAdminPassword
EOF
