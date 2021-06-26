# This script is only useful when SSH has been enabled. This can be done using:
# Manage Jenkins > Configure Global Security > SSH Server > Random

NODE_IP=$(./node_ip.sh)
curl -Lv "http://${NODE_IP}:8080/login" 2>&1 | grep -i "x-ssh-endpoint" | cut -d ':' -f3
