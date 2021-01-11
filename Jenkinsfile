#!/usr/bin/env groovy

pipeline {

    agent any
    
    stages {
        
        stage('Build') {
        
            steps {
            
                withCredentials([
                    file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile'),
                    file(credentialsId: 'java_keystore', variable: 'ORG_GRADLE_PROJECT_keyStore'),
                    file(credentialsId: 'gpg_key', variable: 'ORG_GRADLE_PROJECT_signing.secretKeyRingFile')
                ]) {
            
                    echo 'Building project.'
                    sh './gradlew clean build publish curseforge publishDiluv updateVersionTracker --stacktrace --warn'
                }
            }
        }
    }
}