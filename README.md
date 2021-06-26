# Jenkins on AWS EC2

Forked from one of my old toys: https://github.com/CodeSpyGlass

## Setting things up

### Manual steps

1a) Go into the AWS management console. In here we need to create three things.

- An SSH keypair. Download the ".pem" file for this keypair and save it to this directory as "ec2_key.pem". 
  Run `chmod 400 ec2_key.pem`.
- A security group. Get the identifier for this security group.
- A subnet. Get the identifier for this subnet.

1b) For each of these things, you need to update the appropriate parameter
entries within `jenkins_server.tf`.

1c) Additionally, take a look over the parameters in the `jenkins_server.tf`
terraform file, because they are specific to my usage of it. For example the
code indicates that the EC2 instance will be provisioned in the "ap-southeast-1"
AZ.

2.) Decide on your jenkins login credentials and put them into a file called
"jenkins_creds.txt" in the format `username:password`.

### Automated steps

- Provision everything with `terraform apply` & take it down with
  `terraform destroy`. You can also use `./node_bounce.sh`.
- Set up Jenkins on the EC2 instance with `./node_bootstrap.sh`.
- Create the initial Jenkins user according to the credentials
  in `jenkins_creds.txt` by using `./jenkins_create_user.py`
- Run `./jenkins_install_plugins.sh` to install the required Jenkins plugins.
  The downloads can be flaky, so you may have to ctrl-C the script and re-run
  it, maybe even more than once. Eventually though it'll make it to the end.
- Once all the plugins are installed, go to the update center
  (`jenkins_install_plugins.sh` will print out a link to it at the start of
  script execution), and check the box at the bottom which says that it will
  make Jenkins restart. This should then immediately cause Jenkins to begin
  restarting. The restart should not take long at all, just a few seconds.
- Run `./jenkins_create_job.sh` to create the CodeSpyGlass pipeline job.
- Set up the global pipeline library by running `./jenkins_create_library.py`.
- Get the EC2 IP address using `./node_ip.sh` and go there in the browser to
  port 8080, to finish the Jenkins setup.
- When asked which plugins to install choose "Choose my own plugins", and then
  install none (there's a button near the top to select none if necessary).
- After that, you should be able to start using Jenkins normally. You should be
  able to see and run the 'CodeSpyGlass' pipeline job, and it should go green.

## If necessary

- SSH into the machine with `./node_ssh.sh`.
- Download the Jenkins CLI tool from the Jenkins server using
  `./jenkins_cli_download.sh`.