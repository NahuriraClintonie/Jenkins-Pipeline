pipeline {
    agent any  // Runs on any available Jenkins agent

    environment {
        GITHUB_CREDENTIALS_ID = '256GithubFullAccess'
    }

    stages {
        stage('Checkout') {
                    steps {
                        withCredentials([string(credentialsId: "${GITHUB_CREDENTIALS_ID}", variable: 'GITHUB_TOKEN')]) {
                            script {
                                // Example of using the credentials in a shell script
                                sh 'git config --global url."https://github.com/".insteadOf "git@github.com:"'
                                sh 'git clone https://$GITHUB_TOKEN@github.com/NahuriraClintonie/Jenkins-Pipeline.git'
                            }
                        }
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
