#!/usr/bin/python3
import subprocess

# This script's execution is idempotent.

# This script will fail fast if you try to execute this script without first
# downloading the Jenkins CLI tool using './jenkins_cli_download.sh'.

# It fetches the initial Jenkins admin password by SSHing into the node and
# reading the value from the Docker container which is running Jenkins.

# It uses the initial password to authorize the creation of a new Jenkins user,
# according to the credentials found in the secret file 'jenkins_creds.txt'.

try:
    subprocess.check_call("ls jenkins-cli.jar", shell=True)
except subprocess.CalledProcessError:
    print("Install the Jenkins CLI first, using `./jenkins_cli_download.sh`")
    exit(1)

node_ip = subprocess.check_output("sh node_ip.sh", shell=True) \
    .decode("utf-8").strip()
jenkins_url = f"http://{node_ip}:8080"
initial_password_split = subprocess.check_output(
    "sh jenkins_initial_password.sh", shell=True) \
    .decode("utf-8").strip().split("\n")
initial_password = initial_password_split[len(initial_password_split) - 1]
jenkins_creds_split = subprocess.check_output(
    "cat jenkins_creds.txt",
    shell=True
).decode("utf-8").strip().split(":")
new_jenkins_user = jenkins_creds_split[0]
new_jenkins_password = jenkins_creds_split[1]
shell_command = "echo " \
                "'jenkins.model.Jenkins.instance.securityRealm.createAccount(" \
                "\"{new_jenkins_user}\", \"{new_jenkins_password}\")' | " \
                "java -jar jenkins-cli.jar " \
                "-auth admin:{initial_password} " \
                "-s {jenkins_url}/ " \
                "groovy =" \
    .replace("{new_jenkins_user}", new_jenkins_user) \
    .replace("{new_jenkins_password}", new_jenkins_password) \
    .replace("{initial_password}", initial_password) \
    .replace("{jenkins_url}", jenkins_url) \
    .strip()

subprocess.check_call(shell_command, shell=True)
