pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS = credentials('Github-Credentails') // Use Jenkins credentials
    }

    stages {
        stage('Build Backend') {
            steps {
                script {
                    dir('Backend') {
                        // Run Maven build with debug output
                        sh 'mvn clean install -X'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    dir('Frontend') {
                        // Run Maven build with debug output
                        sh 'mvn clean install -X'
                    }
                }
            }
        }

        stage('Build Root Project') {
            steps {
                script {
                    // Run Maven build with debug output
                    sh 'mvn clean install -X'
                }
            }
        }
    }

    post {
        success {
            script {
                echo 'Build completed successfully!'
                echo "Username: ${GITHUB_CREDENTIALS.username}" // Echo username
                echo "Password: ${GITHUB_CREDENTIALS.password}" // Echo password
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
                echo "Username: ${GITHUB_CREDENTIALS.username}" // Echo username
                echo "Password: ${GITHUB_CREDENTIALS.password}" // Echo password
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
