#!/usr/bin/python3
import subprocess
import requests
import json
import urllib.parse

try:
    subprocess.check_call("ls jenkins-cli.jar", shell=True)
except subprocess.CalledProcessError:
    print("Install the Jenkins CLI first, using `./jenkins_cli_download.sh`")
    exit(1)

node_ip = subprocess.check_output("sh node_ip.sh", shell=True) \
    .decode("utf-8").strip()
jenkins_url = f"http://{node_ip}:8080"


def get_jenkins_credentials():
    with open("jenkins_creds.txt") as f:
        content = f.read().strip()
        split = content.split(":")
        return split[0], split[1]


jenkins_credentials = get_jenkins_credentials()
crumb_response = requests.get(
    jenkins_url + '/crumbIssuer/api/xml'
                  '?xpath=concat(//crumbRequestField,":",//crumb)',
    auth=jenkins_credentials)
jenkins_crumb = crumb_response.text[len("Jenkins-Crumb:"):]
form_data = ["system_message=", "globalMavenOpts=",
             "stapler-class=hudson.maven.local_repo.DefaultLocalRepositoryLocator",
             "%24class=hudson.maven.local_repo.DefaultLocalRepositoryLocator",
             "stapler-class=hudson.maven.local_repo.PerExecutorLocalRepositoryLocator",
             "%24class=hudson.maven.local_repo.PerExecutorLocalRepositoryLocator",
             "stapler-class=hudson.maven.local_repo.PerJobLocalRepositoryLocator",
             "%24class=hudson.maven.local_repo.PerJobLocalRepositoryLocator",
             "_.numExecutors=2", "_.labelString=", "master.mode=NORMAL",
             "_.quietPeriod=5", "_.scmCheckoutRetryCount=0", "namingStrategy=0",
             "stapler-class=jenkins.model.ProjectNamingStrategy%24DefaultProjectNamingStrategy",
             "%24class=jenkins.model.ProjectNamingStrategy%24DefaultProjectNamingStrategy",
             "stapler-class=jenkins.model.ProjectNamingStrategy%24PatternProjectNamingStrategy",
             "%24class=jenkins.model.ProjectNamingStrategy%24PatternProjectNamingStrategy",
             "_.namePattern=.*", "_.description=",
             "_.url=http%3A%2F%2F" + node_ip + "%3A8080%2F",
             "_.adminAddress=address+not+configured+yet+%3Cnobody%40nowhere%3E",
             "_.url=", "durabilityHint=null", "_.usageStatisticsCollected=on",
             "_.systemTimeFormat=%27%3Cb%3E%27HH%3Amm%3Ass%27%3C%2Fb%3E+%27",
             "_.elapsedTimeFormat=%27%3Cb%3E%27HH%3Amm%3Ass.S%27%3C%2Fb%3E+%27",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "administrativeMonitor=on", "administrativeMonitor=on",
             "stapler-class=jenkins.model.JobGlobalBuildDiscarderStrategy",
             "%24class=jenkins.model.JobGlobalBuildDiscarderStrategy",
             "_.hookUrl=http%3A%2F%2F" + node_ip + "%3A8080%2Fgithub-webhook%2F",
             "_.apiRateLimitChecker=ThrottleForNormalize",
             "_.name=CodeSpyGlass",
             "_.defaultVersion=main", "_.allowVersionOverride=on",
             "_.includeInChangesets=on", "removeme1_retriever=0",
             "stapler-class=org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever",
             "%24class=org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever",
             "stapler-class=jenkins.plugins.git.GitSCMSource",
             "%24class=jenkins.plugins.git.GitSCMSource",
             "id=7c9e6cff-a0c5-4023-ac1b-ea569c02b995", "_.remote=",
             "includeUser=false", "_.credentialsId=",
             "stapler-class=jenkins.plugins.git.traits.BranchDiscoveryTrait",
             "%24class=jenkins.plugins.git.traits.BranchDiscoveryTrait",
             "removeme1_scm=1",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.GitHubSCMSource",
             "%24class=org.jenkinsci.plugins.github_branch_source.GitHubSCMSource",
             "id=c670ddd4-0e6f-4f5d-9df7-d2040f2f12f3", "includeUser=false",
             "_.credentialsId=", "configuredByUrl=true",
             "removeme1_configuredByUrlRadio=true",
             "_.repositoryUrl=https%3A%2F%2Fgithub.com%2FCodeSpyGlass%2FJenkinsPipelineLibrary",
             "_.repoOwner=CodeSpyGlass", "_.strategyId=1",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait",
             "%24class=org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait",
             "_.strategyId=1",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.OriginPullRequestDiscoveryTrait",
             "%24class=org.jenkinsci.plugins.github_branch_source.OriginPullRequestDiscoveryTrait",
             "_.strategyId=1",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustContributors",
             "%24class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustContributors",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustEveryone",
             "%24class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustEveryone",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustPermission",
             "%24class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustPermission",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustNobody",
             "%24class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait%24TrustNobody",
             "stapler-class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait",
             "%24class=org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait",
             "stapler-class=org.jenkinsci.plugins.workflow.libs.SCMRetriever",
             "%24class=org.jenkinsci.plugins.workflow.libs.SCMRetriever",
             "stapler-class=hudson.plugins.git.GitSCM",
             "%24class=hudson.plugins.git.GitSCM", "_.url=",
             "includeUser=false",
             "_.credentialsId=", "_.name=", "_.refspec=", "_.name=*%2Fmaster",
             "stapler-class=hudson.plugins.git.browser.AssemblaWeb",
             "%24class=hudson.plugins.git.browser.AssemblaWeb",
             "stapler-class=hudson.plugins.git.browser.FisheyeGitRepositoryBrowser",
             "%24class=hudson.plugins.git.browser.FisheyeGitRepositoryBrowser",
             "stapler-class=hudson.plugins.git.browser.KilnGit",
             "%24class=hudson.plugins.git.browser.KilnGit",
             "stapler-class=hudson.plugins.git.browser.TFS2013GitRepositoryBrowser",
             "%24class=hudson.plugins.git.browser.TFS2013GitRepositoryBrowser",
             "stapler-class=hudson.plugins.git.browser.BitbucketWeb",
             "%24class=hudson.plugins.git.browser.BitbucketWeb",
             "stapler-class=hudson.plugins.git.browser.CGit",
             "%24class=hudson.plugins.git.browser.CGit",
             "stapler-class=hudson.plugins.git.browser.GitBlitRepositoryBrowser",
             "%24class=hudson.plugins.git.browser.GitBlitRepositoryBrowser",
             "stapler-class=hudson.plugins.git.browser.GithubWeb",
             "%24class=hudson.plugins.git.browser.GithubWeb",
             "stapler-class=hudson.plugins.git.browser.Gitiles",
             "%24class=hudson.plugins.git.browser.Gitiles",
             "stapler-class=hudson.plugins.git.browser.GitLab",
             "%24class=hudson.plugins.git.browser.GitLab",
             "stapler-class=hudson.plugins.git.browser.GitList",
             "%24class=hudson.plugins.git.browser.GitList",
             "stapler-class=hudson.plugins.git.browser.GitoriousWeb",
             "%24class=hudson.plugins.git.browser.GitoriousWeb",
             "stapler-class=hudson.plugins.git.browser.GitWeb",
             "%24class=hudson.plugins.git.browser.GitWeb",
             "stapler-class=hudson.plugins.git.browser.GogsGit",
             "%24class=hudson.plugins.git.browser.GogsGit",
             "stapler-class=hudson.plugins.git.browser.Phabricator",
             "%24class=hudson.plugins.git.browser.Phabricator",
             "stapler-class=hudson.plugins.git.browser.RedmineWeb",
             "%24class=hudson.plugins.git.browser.RedmineWeb",
             "stapler-class=hudson.plugins.git.browser.RhodeCode",
             "%24class=hudson.plugins.git.browser.RhodeCode",
             "stapler-class=hudson.plugins.git.browser.Stash",
             "%24class=hudson.plugins.git.browser.Stash",
             "stapler-class=hudson.plugins.git.browser.ViewGitWeb",
             "%24class=hudson.plugins.git.browser.ViewGitWeb",
             "_.showNeedToCloneInformation=on", "_.globalConfigName=",
             "_.globalConfigEmail=", "_.shell=", "_.address=", "_.smtpHost=",
             "_.smtpPort=25", "_.smtpUsername=",
             "_.smtpPassword=%7BAQAAABAAAAAQrmHza2jy0CYOl5FVBUGD0Rsh48nh%2FJR1NRXO4LLVvF0%3D%7D",
             "_.advProperties=", "_.defaultSuffix=", "_.charset=UTF-8",
             "_.defaultContentType=text%2Fplain", "_.listId=",
             "_.defaultRecipients=",
             "_.defaultReplyTo=", "_.emergencyReroute=", "_.allowedDomains=",
             "_.excludedCommitters=",
             "_.defaultSubject=%24PROJECT_NAME+-+Build+%23+%24BUILD_NUMBER+-+%24BUILD_STATUS%21",
             "_.maxAttachmentSizeMb=-1",
             "_.defaultBody=%24PROJECT_NAME+-+Build+%23+%24BUILD_NUMBER+-+%24BUILD_STATUS%3A%0D%0A%0D%0ACheck+console+output+at+%24BUILD_URL+to+view+the+results.",
             "_.defaultPresendScript=", "_.defaultPostsendScript=",
             "_.defaultTriggerIds=on", "_.smtpHost=", "_.defaultSuffix=",
             "_.username=",
             "_.password=", "_.smtpPort=", "_.replyToAddress=",
             "_.charset=UTF-8",
             "sendTestMailTo=", "core%3Aapply=true",
             "Jenkins-Crumb=" + jenkins_crumb]

form_data_dict = {}
for entry in form_data:
    split = entry.split("=")
    if len(split) == 1:
        form_data_dict[entry[:-1]] = ""
    else:
        form_data_dict[split[0]] = split[1]

encoded_json_form_data = urllib.parse.quote_plus(json.dumps(form_data_dict))
form_data_dict["json"] = encoded_json_form_data

headers = {
    "Content-Type": "application/x-www-form-urlencoded",
    "Cookie": crumb_response.headers["Set-Cookie"]
}
config_submit_response = requests.post(
    jenkins_url + '/configSubmit',
    data=form_data_dict,
    auth=jenkins_credentials,
    headers=headers)

print(config_submit_response.text)
print(config_submit_response.headers)
print(config_submit_response.status_code)
