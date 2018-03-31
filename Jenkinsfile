pipeline {
  agent {
    label 'master'
  }
  triggers {
    //pollSCM needed due to https://mohamicorp.atlassian.net/wiki/spaces/DOC/pages/381288449/Configuring+Webhook+To+Jenkins+for+Bitbucket+Git+Plugin
    pollSCM('0 0 1 1 *')
  }
  stages {
    stage('Build') {
      steps {
        echo 'Build'

        withMaven(
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            maven: 'mvn',
            // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
            // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
            // mavenSettingsConfig: 'my-maven-settings',
            mavenLocalRepo: '.repository') {

          // Run the maven build
          sh "mvn clean install"

        } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe & FindBugs reports...

      }
    }
  }
}
