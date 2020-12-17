JENKINS_IP=$(./jenkins_ip.sh)

# Copy over ObjectCalisthenicsAnalyser JAR file
scp -i jenkins_server_keys.pem \
  ../ObjectCalisthenicsAnalyser/build/libs/ObjectCalisthenicsAnalyser-1.0-SNAPSHOT.jar \
  ubuntu@${JENKINS_IP}:/home/ubuntu

# Set up Jenkins and run it alongside the analyser
ssh -i jenkins_server_keys.pem ubuntu@${JENKINS_IP} << 'EOF'
  ### Start Jenkins ###
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
  sudo docker run -d -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts

  ### Start ObjectCalisthenicsAnalyser ###
  sudo add-apt-repository -y ppa:openjdk-r/ppa
  sudo apt-get update
  sudo apt install -y openjdk-11-jdk
  nohup java -jar ObjectCalisthenicsAnalyser-1.0-SNAPSHOT.jar > analyser-log.txt &
EOF
