NODE_IP=$(./node_ip.sh)
ssh -i ec2_key.pem "ubuntu@${NODE_IP}" <<'EOF'
  cat analyser-log.txt
EOF
