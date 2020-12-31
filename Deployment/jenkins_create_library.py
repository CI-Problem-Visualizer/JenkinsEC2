#!/usr/bin/python3
import subprocess
import requests
import json

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
form_data_dict = {"system_message": "",
                  "hudson-maven-MavenModuleSet": {"globalMavenOpts": "",
                                                  "": "0",
                                                  "localRepository": {
                                                      "stapler-class": "hudson.maven.local_repo.DefaultLocalRepositoryLocator",
                                                      "$class": "hudson.maven.local_repo.DefaultLocalRepositoryLocator"}},
                  "jenkins-model-MasterBuildConfiguration": {
                      "numExecutors": "2", "labelString": "",
                      "mode": "NORMAL"},
                  "jenkins-model-GlobalQuietPeriodConfiguration": {
                      "quietPeriod": "5"},
                  "jenkins-model-GlobalSCMRetryCountConfiguration": {
                      "scmCheckoutRetryCount": "0"},
                  "jenkins-model-GlobalProjectNamingStrategyConfiguration": {},
                  "jenkins-model-JenkinsLocationConfiguration": {
                      "url": jenkins_url + "/",
                      "adminAddress": "address not configured yet <nobody@nowhere>"},
                  "jenkins-security-ResourceDomainConfiguration": {"url": ""},
                  "jenkins-model-GlobalNodePropertiesConfiguration": {
                      "globalNodeProperties": {}},
                  "org-jenkinsci-plugins-workflow-flow-GlobalDefaultFlowDurabilityLevel": {
                      "durabilityHint": "null"},
                  "hudson-model-UsageStatistics": {
                      "usageStatisticsCollected": {}},
                  "hudson-plugins-timestamper-TimestamperConfig": {
                      "systemTimeFormat": "'<b>'HH:mm:ss'</b> '",
                      "elapsedTimeFormat": "'<b>'HH:mm:ss.S'</b> '",
                      "allPipelines": False},
                  "jenkins-fingerprints-GlobalFingerprintConfiguration": {
                      "fingerprintCleanupDisabled": False},
                  "jenkins-management-AdministrativeMonitorsConfiguration": {
                      "administrativeMonitor": [
                          "jenkins.security.QueueItemAuthenticatorMonitor",
                          "AsyncResourceDisposer",
                          "jenkins.security.csrf.CSRFAdministrativeMonitor",
                          "jenkins.diagnostics.URICheckEncodingMonitor",
                          "hudson.triggers.SlowTriggerAdminMonitor",
                          "hudson.PluginManager$PluginCycleDependenciesMonitor",
                          "hudson.PluginManager$PluginDeprecationMonitor",
                          "jenkins.security.s2m.MasterKillSwitchWarning",
                          "jenkins.diagnostics.SecurityIsOffMonitor",
                          "hudsonHomeIsFull",
                          "jenkins.model.Jenkins$EnforceSlaveAgentPortAdministrativeMonitor",
                          "GitHubHookRegisterProblemMonitor",
                          "hudson.PluginManager$PluginUpdateMonitor",
                          "jenkins.diagnosis.HsErrPidList",
                          "jenkins.diagnostics.CompletedInitializationMonitor",
                          "hudson.model.UpdateCenter$CoreUpdateMonitor",
                          "jenkins.security.apitoken.ApiTokenPropertyEnabledNewLegacyAdministrativeMonitor",
                          "jenkins.security.apitoken.ApiTokenPropertyDisabledDefaultAdministrativeMonitor",
                          "legacyApiToken", "OldData",
                          "hudson.diagnosis.NullIdDescriptorMonitor",
                          "hudson.node_monitors.MonitorMarkedNodeOffline",
                          "hudson.PluginWrapper$PluginWrapperAdministrativeMonitor",
                          "jenkins.security.RekeySecretAdminMonitor",
                          "slaveToMasterAccessControl",
                          "jenkins.security.ResourceDomainRecommendation",
                          "hudson.diagnosis.ReverseProxySetupMonitor",
                          "jenkins.diagnostics.RootUrlNotSetMonitor",
                          "hudson.diagnosis.TooManyJobsButNoView",
                          "hudson.triggers.SCMTrigger$AdministrativeMonitorImpl",
                          "jenkins.security.UpdateSiteWarningsMonitor",
                          "jenkins.branch.PropertyMigration$MonitorImpl"]},
                  "jenkins-model-GlobalBuildDiscarderConfiguration": {
                      "configuredBuildDiscarders": {
                          "stapler-class": "jenkins.model.JobGlobalBuildDiscarderStrategy",
                          "$class": "jenkins.model.JobGlobalBuildDiscarderStrategy"}},
                  "github-plugin-configuration": {"": False,
                                                  "hookUrl": jenkins_url + "/github-webhook/"},
                  "org-jenkinsci-plugins-github_branch_source-GitHubConfiguration": {
                      "apiRateLimitChecker": "ThrottleForNormalize"},
                  "org-jenkinsci-plugins-workflow-libs-GlobalLibraries": {
                      "libraries": {"name": "CodeSpyGlass",
                                    "defaultVersion": "main",
                                    "implicit": False,
                                    "allowVersionOverride": True,
                                    "includeInChangesets": True,
                                    "retriever": {"value": "0",
                                                  "stapler-class": "org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever",
                                                  "$class": "org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever",
                                                  "scm": {"value": "1",
                                                          "stapler-class": "org.jenkinsci.plugins.github_branch_source.GitHubSCMSource",
                                                          "$class": "org.jenkinsci.plugins.github_branch_source.GitHubSCMSource",
                                                          "id": "c670ddd4-0e6f-4f5d-9df7-d2040f2f12f3",
                                                          "includeUser": "false",
                                                          "credentialsId": "",
                                                          "configuredByUrl": "true",
                                                          "configuredByUrlRadio": "true",
                                                          "repositoryUrl": "https://github.com/CodeSpyGlass/JenkinsPipeline",
                                                          "repoOwner": "CodeSpyGlass",
                                                          "repository": "",
                                                          "traits": [{
                                                              "strategyId": "1",
                                                              "stapler-class": "org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait",
                                                              "$class": "org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait"},
                                                              {
                                                                  "strategyId": "1",
                                                                  "stapler-class": "org.jenkinsci.plugins.github_branch_source.OriginPullRequestDiscoveryTrait",
                                                                  "$class": "org.jenkinsci.plugins.github_branch_source.OriginPullRequestDiscoveryTrait"},
                                                              {
                                                                  "strategyId": "1",
                                                                  "": "2",
                                                                  "trust": {
                                                                      "stapler-class": "org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait$TrustPermission",
                                                                      "$class": "org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait$TrustPermission"},
                                                                  "stapler-class": "org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait",
                                                                  "$class": "org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait"}]}}}},
                  "net-uaznia-lukanus-hudson-plugins-gitparameter-GitParameterDefinition": {
                      "showNeedToCloneInformation": True},
                  "hudson-plugins-build_timeout-operations-BuildStepOperation": {},
                  "hudson-plugins-git-GitSCM": {"globalConfigName": "",
                                                "globalConfigEmail": "",
                                                "createAccountBasedOnEmail": False,
                                                "useExistingAccountWithSameEmail": False,
                                                "showEntireCommitSummaryInChanges": False,
                                                "hideCredentials": False,
                                                "disableGitToolChooser": False,
                                                "allowSecondFetch": False,
                                                "addGitTagAction": False},
                  "hudson-tasks-Shell": {"shell": ""},
                  "hudson-plugins-emailext-ExtendedEmailPublisher": {
                      "mailAccount": {"address": "", "smtpHost": "",
                                      "smtpPort": "25", "smtpUsername": "",
                                      "smtpPassword": "{AQAAABAAAAAQrmHza2jy0CYOl5FVBUGD0Rsh48nh/JR1NRXO4LLVvF0=}",
                                      "$redact": "smtpPassword",
                                      "useSsl": False, "advProperties": ""},
                      "defaultSuffix": "", "charset": "UTF-8",
                      "defaultContentType": "text/plain", "listId": "",
                      "precedenceBulk": False, "defaultRecipients": "",
                      "defaultReplyTo": "", "emergencyReroute": "",
                      "allowedDomains": "", "excludedCommitters": "",
                      "defaultSubject": "$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!",
                      "maxAttachmentSizeMb": "-1",
                      "defaultBody": "$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:\n\nCheck console output at $BUILD_URL to view the results.",
                      "defaultPresendScript": "", "defaultPostsendScript": "",
                      "debugMode": False,
                      "adminRequiredForTemplateTesting": False,
                      "watchingEnabled": False,
                      "allowUnregisteredEnabled": False,
                      "defaultTriggerIds": "hudson.plugins.emailext.plugins.trigger.FailureTrigger"},
                  "hudson-tasks-Mailer": {"smtpHost": "", "defaultSuffix": "",
                                          "useSsl": False, "useTls": False,
                                          "smtpPort": "",
                                          "replyToAddress": "",
                                          "charset": "UTF-8"},
                  "core:apply": "true", "Jenkins-Crumb": jenkins_crumb}

form_data_json = json.dumps(form_data_dict)
form_data_dict["json"] = form_data_json
config_submit_response = requests.post(
    jenkins_url + '/configSubmit',
    data=form_data_dict,
    auth=jenkins_credentials,
    headers={
        "Content-Type": "application/x-www-form-urlencoded",
        "Cookie": crumb_response.headers["Set-Cookie"]
    })
print("Jenkins responded to upload with HTTP status: " +
      str(config_submit_response.status_code))
