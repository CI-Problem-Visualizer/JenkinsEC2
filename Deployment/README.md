# Jenkins on AWS

## Setting things up

- Provision everything with `terraform apply` & take it down with `terraform destroy`
- Set up Jenkins on the EC2 instance with `./jenkins_bootstrap.sh`
- (If necessary) SSH into the machine with `./ssh.sh`
- Get the initial password for Jenkins using `./jenkins_initial_password.sh`
- Get the EC2 IP address using `./jenkins_ip.sh` and go there in the browser
- When asked which plugins to install choose "Choose my own plugins", and then install none.
There is a script to install exactly the set of plugins we need.
- Set up the admin user with the following details:

```
Username: JenkinsAdmin
Password: (See jenkins_creds.txt)
Confirm Password: (See jenkins_creds.txt)
Full name: JenkinsAdmin
```

- (If necessary) Download the Jenkins CLI tool from the Jenkins server using `./jenkins_cli_download.sh`
- Run `./jenkins_install_plugins.sh` to install the required Jenkins plugins. The downloads can be flaky, so
you may have to ctrl-C the script and re-run it, maybe even more than once. Eventually though it'll make it to the end.
- Run `./jenkins_create_job.sh CodeSpyGlass_config.xml` to create the CodeSpyGlass pipeline job
- Set up the global pipeline library by going to 'Manage Jenkins > Configure System > Global Pipeline Libraries' and configure it as follows:

```
Name: CodeSpyGlass
Default version: main

Allow default version to be overridden: Yes
Include @Library changes in job recent changes: Yes

Retrieval method: Modern SCM
Source code management:
  GitHub:
    Repository HTTPS URL: https://github.com/CodeSpyGlass/JenkinsPipelineLibrary
    (leave everything else at the defaults)
```

- Now you should be able to run the 'CodeSpyGlass' pipeline job, and it should go green.
