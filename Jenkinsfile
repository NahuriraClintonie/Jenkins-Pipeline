pipeline {
    agent any

    environment {
        // Hard-coded GitHub credentials
        GITHUB_USERNAME = 'NahuriraClintonie'
        GITHUB_PASSWORD = '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'
    }

    stages {
        stage('Setup Git Configuration') {
            steps {
                script {
                    // Increase Git buffer size
                    sh 'git config --global http.postBuffer 3048576000' // 3 GB
                    // Optionally, increase the timeout settings
                    sh 'git config --global http.lowSpeedLimit 0'
                    sh 'git config --global http.lowSpeedTime 999999'
                }
            }
        }
        
        stage('Build Backend') {
            steps {
                script {
                    dir('Backend') {
                        // Run Maven build with debug output
                        sh "mvn package -Dgithub.username=${GITHUB_USERNAME} -Dgithub.token=${GITHUB_TOKEN}"
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    dir('Frontend') {
                        // Run Maven build with debug output
                        sh "mvn package -Dgithub.username=${GITHUB_USERNAME} -Dgithub.token=${GITHUB_TOKEN}"
                    }
                }
            }
        }

        stage('Build Root Project') {
            steps {
                script {
                    // Run Maven build with debug output
                    sh "mvn package -Dgithub.username=${GITHUB_USERNAME} -Dgithub.token=${GITHUB_TOKEN}"
                }
            }
        }
    }

    post {
        success {
            script {
                echo 'Project Build and Packaged completed successfully!'
                echo "Username: ${GITHUB_USERNAME}" // Echo username
                echo "Password: ${GITHUB_PASSWORD}" // Echo password
                emailext (
                    subject: "Build Success: ${currentBuild.fullDisplayName}",
                    body: """
                        <p>Good news! The build was successful.</p>
                        <p>Job: ${env.JOB_NAME}</p>
                        <p>Build Number: ${env.BUILD_NUMBER}</p>
                        <p>Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    """,
                    to: 'clintonnahurira24@gmail.com',
                    mimeType: 'text/html'
                )
            }
        }

        failure {
            script {
                echo 'Build failed. Please check the logs for more details.'
                echo "Username: ${GITHUB_USERNAME}" // Echo username
                echo "Password: ${GITHUB_PASSWORD}" // Echo password
                emailext (
                    subject: "Build Failed: ${currentBuild.fullDisplayName}",
                    body: """
                        <p>Unfortunately, the build failed.</p>
                        <p>Job: ${env.JOB_NAME}</p>
                        <p>Build Number: ${env.BUILD_NUMBER}</p>
                        <p>Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p>Please check the attached logs to investigate the issue.</p>
                    """,
                    to: 'clintonnahurira24@gmail.com',
                    mimeType: 'text/html',
                    attachLog: true
                )
            }
        }

        always {
            cleanWs()  // Clean up the workspace after the build
        }
    }
}
