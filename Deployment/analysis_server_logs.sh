NODE_IP=$(./node_ip.sh)
ssh -i jenkins_server_keys.pem "ubuntu@${NODE_IP}" <<'EOF'
  cat analyser-log.txt
EOF
