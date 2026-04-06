#!/usr/bin/env groovy
pipeline {
    agent any
    tools {
        jdk "jdk-25"
    }
    stages {
        stage('Setup') {
            steps {
                echo 'Setup Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Build') {
            steps {
                withCredentials([
                    file(credentialsId: 'build_secrets_v2', variable: 'ORG_GRADLE_PROJECT_secretFileV2'),
                    file(credentialsId: 'java_keystore', variable: 'ORG_GRADLE_PROJECT_keyStore'),
                    file(credentialsId: 'gpg_key', variable: 'ORG_GRADLE_PROJECT_pgpKeyRing')
                ]) {
                    echo 'Building project.'
                    sh './gradlew build publish publishCurseForge modrinth updateVersionTracker --stacktrace --warn'
                }
            }
        }
    }
}