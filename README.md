# Jenkins on AWS EC2

Forked from one of my old toys: https://github.com/CodeSpyGlass

## Setting things up

Your environment needs to have some tools. You can verify that you have them by running `check_dependencies.sh`. It 
will say "All good." if your environment has everything that's needed.

### Manual steps

1a) Go into the AWS management console. In here we need to create three things.

- An SSH keypair. Download the ".pem" file for this keypair and save it to this directory as `ec2_key.pem`. 
  Run `chmod 400 ec2_key.pem`.
- A security group. Get the identifier for this security group.
- A subnet. Get the identifier for this subnet.

1b) For each of these things, you need to update the appropriate parameter
entries within `jenkins_server.tf`.

1c) Additionally, take a look over the parameters in the `jenkins_server.tf`
terraform file, because they are specific to my usage of it. For example the
code indicates that the EC2 instance will be provisioned in the "ap-southeast-1"
AZ.

2) Decide on your jenkins login credentials and put them into a file called
"jenkins_creds.txt" in the format `username:password`. For example, you could
run `echo "jenkins:superpassword" > jenkins_creds.txt` to create this file with
a user 'jenkins', with password 'superpassword'.

### Automated steps

- Provision everything with `terraform apply` & take it down with
  `terraform destroy`. You can also use `./node_bounce.sh`.
- Set up Jenkins on the EC2 instance with `./node_bootstrap.sh`.
- Download the Jenkins CLI tool from the new Jenkins server using
  `./jenkins_cli_download.sh`.
- Create the initial Jenkins user according to the credentials
  in `jenkins_creds.txt` by using `./jenkins_create_user.py`
- Run `./jenkins_install_plugins.sh` to install the required Jenkins plugins.
  The downloads can be flaky, so you may have to ctrl-C the script and re-run
  it, maybe even more than once. Eventually though it'll make it to the end.
- Once Jenkins has installed all the plugins, go to the update center
  (`jenkins_install_plugins.sh` will print out a link to it at the start of
  script execution). Check the box at the bottom which says it'll make
  Jenkins restart. This should then immediately cause Jenkins to begin
  restarting. The restart should not take long at all, just a few seconds.
- Open Jenkins in a browser. You can use the script `./jenkins_open_in_browser.sh`
  or you can get the EC2 IP address using `./node_ip.sh` and go there manually 
  in the browser to port 8080, to finish the Jenkins setup as described below.
- When asked which plugins to install, choose "Choose my own plugins". Choose
  'none' (there's a button near the top to select none if necessary) and then
  press the button to proceed.
- When asked about the Jenkins URL, continue with the default value.
- After that, you should be able to start using Jenkins normally.

## If necessary

- SSH into the machine with `./node_ssh.sh`.
- Set up a global pipeline library using `./jenkins_create_library.py <library repository url>`.