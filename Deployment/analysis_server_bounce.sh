NODE_IP=$(./node_ip.sh)

if [[ $NODE_IP == "" ]]; then
  echo "
Can't deploy. The node isn't up, so there's nowhere to deploy to. Bring the
server up first using the scripts as described in Deployment/README.md.
"
  exit 1
fi

# Rebuild
pushd ../JavaAnalyser || exit 1
./gradlew jar
popd || exit 1

# Copy over JAR
scp -i jenkins_server_keys.pem \
  ../JavaAnalyser/build/libs/JavaAnalyser-1.0-SNAPSHOT.jar \
  "ubuntu@${NODE_IP}:/home/ubuntu"

# Run JAR
ssh -i jenkins_server_keys.pem "ubuntu@${NODE_IP}" <<'EOF'
  command -v java
  JAVA_MISSING=$?
  if [[ $JAVA_MISSING == "1" ]]
  then
    echo "Installing Java"
    sudo add-apt-repository -y ppa:openjdk-r/ppa
    sudo apt-get update
    sudo apt install -y openjdk-11-jdk
  fi

  PORT=$(lsof -t -i :9000)
  if [[ $PORT != "" ]]
  then
    echo "Killing previously running analysis server"
    kill $PORT
  fi

  echo "Running analysis server"
  nohup java -jar JavaAnalyser-1.0-SNAPSHOT.jar > analyser-log.txt 2>&1 &
  sleep 3
  cat analyser-log.txt
EOF
