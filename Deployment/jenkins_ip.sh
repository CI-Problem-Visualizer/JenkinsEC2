cat terraform.tfstate | grep -A 1 jenkins_server_ip | tail -1 | tr -d ' ' | cut -d '"' -f4
