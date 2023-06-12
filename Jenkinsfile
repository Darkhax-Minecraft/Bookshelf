#!/usr/bin/env groovy

pipeline {

    agent any

    tools {
        jdk "jdk-17.0.1"
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
                    file(credentialsId: 'build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile'),
                    file(credentialsId: 'java_keystore', variable: 'ORG_GRADLE_PROJECT_keyStore'),
                    file(credentialsId: 'gpg_key', variable: 'ORG_GRADLE_PROJECT_pgpKeyRing')
                ]) {
            
                    echo 'Building project.'
                    sh './gradlew build --stacktrace --warn'
                }
            }
        }
        
        stage('Publish') {
        
            steps {
            
                withCredentials([file(credentialsId: 'build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                
                    echo 'Updating version'
                    sh './gradlew updateVersionTracker'

                    echo 'Deploying to Maven'
                    sh './gradlew publish'

                    echo 'Deploying to CurseForge'
                    sh './gradlew publishCurseForge'
                    
                    echo 'Deploying to Modrinth'
                    sh './gradlew modrinth'
                    
                    echo 'Discord Announcement'
                    sh './gradlew postDiscord'
                }
            }
        }
    }
}
