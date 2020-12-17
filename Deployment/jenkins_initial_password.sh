JENKINS_IP=$(./jenkins_ip.sh)
ssh -i jenkins_server_keys.pem ubuntu@${JENKINS_IP} << 'EOF'
  CONTAINER_ID=$(sudo docker ps | grep "jenkins/jenkins:lts" | cut -d " " -f1)
  sudo docker exec ${CONTAINER_ID} cat /var/jenkins_home/secrets/initialAdminPassword
EOF
