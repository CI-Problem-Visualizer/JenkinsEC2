# Jenkins on AWS

## Setting things up

- Provision everything with `terraform apply` & take it down with `terraform destroy`
- Set up Jenkins on the EC2 instance with `./jenkins_bootstrap.sh`
- (If necessary) SSH into the machine with `./ssh.sh`
- Get the initial password for Jenkins using `./jenkins_initial_password.sh`
- Get the EC2 IP address using `./jenkins_ip.sh` and go there in the browser
- Set up the admin user with the following details:

```
Username: JenkinsAdmin
Password: (See jenkins_creds.txt)
Confirm Password: (See jenkins_creds.txt)
Full name: JenkinsAdmin
E-mail address: example@example.com
```

- Click through the rest of the setup with just the defaults until you get to the dashboard and you're ready to go
- (If necessary) Download the Jenkins CLI tool using `./jenkins_cli_download.sh`
- Run `./jenkins_install_plugins.sh` to install the extra Jenkins plugins
- Run `./jenkins_create_job.sh CodeEvaluation_config.xml` to create the CodeEvaluation pipeline job
- Set up the global pipeline library by going to 'Manage Jenkins > Configure System > Global Pipeline Libraries' and configure it as follows:

```
Name: CodeEvaluation
Default version: main

Allow default version to be overridden: Yes
Include @Library changes in job recent changes: Yes

Retrieval method: Modern SCM
Source code management:
  GitHub:
    Repository HTTPS URL: https://github.com/CodeEvaluation/JenkinsPipelineLibrary
    (leave everything else at the defaults)
```

- Now you should be able to run the 'CodeEvaluation' pipeline job, and it should go green.
