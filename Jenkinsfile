pipeline {
    agent any

    stages {
        stage('Build Backend') {
            steps {
                script {
                    echo 'Starting Backend Build...'
                    dir('Backend') {
                        sh 'mvn clean install'
                    }
                    echo 'Backend Build completed!'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    echo 'Starting Frontend Build...'
                    dir('Frontend') {
                        sh 'mvn clean install'
                    }
                    echo 'Frontend Build completed!'
                }
            }
        }

        stage('Build Root Project') {
            steps {
                script {
                    echo 'Starting Root Project Build...'
                    sh 'mvn clean install'
                    echo 'Root Project Build completed!'
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
