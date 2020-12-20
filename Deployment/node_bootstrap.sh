NODE_IP=$(./node_ip.sh)

### Install and start Jenkins ###
ssh -i jenkins_server_keys.pem "ubuntu@${NODE_IP}" <<'EOF'
  sudo apt update
  sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
  sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
  sudo apt update
  sudo apt upgrade -y
  sudo apt install -y docker-ce
  apt-cache policy docker-ce > /home/ubuntu/docker-ce-cache-policy.log
  sudo systemctl status docker > /home/ubuntu/docker-service-status.log
  sudo docker pull jenkins/jenkins:lts
  sudo ufw allow 8080
  sudo docker run -d --network host -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts
EOF

### Install and start analysis server ###
./analysis_server_bounce.sh
