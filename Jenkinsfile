pipeline {
    agent any  // Runs on any available Jenkins agent

    environment {
        // Define any environment variables if needed
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the Git repository using SSH URL
                git url: 'https://github.com/NahuriraClintonie/Jenkins-Pipeline.git', branch: 'main'
            }
        }

        stage('Build Backend') {
            steps {
                dir('Backend') {
                    // Navigate to the Backend directory and run Maven build
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Frontend') {
                    // Navigate to the Frontend directory and run Maven build
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Root Project') {
            steps {
                // Navigate to the root directory and run Maven build
                sh 'mvn clean install'
            }
        }
    }

    post {
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
        always {
            cleanWs()  // Clean up the workspace after the build
        }
    }
}
