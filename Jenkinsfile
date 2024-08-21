pipeline {
    agent any

    stages {
        stage('Build Backend') {
            steps {
                script {
                    dir('Backend') {
                        sh 'mvn clean install'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    dir('Frontend') {
                        sh 'mvn clean install'
                    }
                }
            }
        }

        stage('Build Root Project') {
            steps {
                script {
                    sh 'mvn clean install'
                }
            }
        }
    }

    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed. Please check the logs for more details.'
        }
    }
}
