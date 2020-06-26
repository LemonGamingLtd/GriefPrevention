pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-11'
        }
    }

    stages {
        stage('Build') {
            steps {
                withMaven(jdk: "JDK11", maven: 'maven', mavenSettingsConfig: 'maven-global-settings-xml') {
                    sh '$MVN_CMD clean install deploy --update-snapshots'
                }
            }
        }
    }
}
