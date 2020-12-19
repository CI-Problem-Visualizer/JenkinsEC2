grep -A 1 jenkins_server_ip terraform.tfstate | tail -1 | tr -d ' ' | cut -d '"' -f4
