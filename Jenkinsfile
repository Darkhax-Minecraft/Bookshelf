#!/usr/bin/env groovy

pipeline {

    agent any

    tools {
        jdk "jdk8u292-b10"
    }
    
    stages {
        
        stage('Build') {
        
            steps {
            
                withCredentials([
                    file(credentialsId: 'build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile'),
                    file(credentialsId: 'java_keystore', variable: 'ORG_GRADLE_PROJECT_keyStore'),
                    file(credentialsId: 'gpg_key', variable: 'ORG_GRADLE_PROJECT_pgpKeyRing')
                ]) {
            
                    echo 'Building project.'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build publish curseforge updateVersionTracker postTweet --stacktrace --warn'
                }
            }
        }
    }
}
