JENKINS_IP=$(./jenkins_ip.sh)
ssh -i jenkins_server_keys.pem ubuntu@"${JENKINS_IP}" << 'EOF'
  cat analyser-log.txt
EOF
