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
                        // Create a custom settings.xml for Maven
                        writeFile file: '/root/.m2/settings.xml', text: """
                        <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
                                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
                            <servers>
                                <server>
                                    <id>github</id>
                                    <username>${GITHUB_CREDENTIALS.username}</username>
                                    <password>${GITHUB_CREDENTIALS.password}</password>
                                </server>
                            </servers>
                        </settings>
                        """
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
                        // Create a custom settings.xml for Maven
                        writeFile file: '/root/.m2/settings.xml', text: """
                        <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
                                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
                            <servers>
                                <server>
                                    <id>github</id>
                                    <username>${GITHUB_CREDENTIALS.username}</username>
                                    <password>${GITHUB_CREDENTIALS.password}</password>
                                </server>
                            </servers>
                        </settings>
                        """
                        // Run Maven build with debug output
                        sh 'mvn clean install -X'
                    }
                }
            }
        }

        stage('Build Root Project') {
            steps {
                script {
                    // Create a custom settings.xml for Maven
                    writeFile file: '/root/.m2/settings.xml', text: """
                    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
                              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
                        <servers>
                            <server>
                                <id>github</id>
                                <username>${GITHUB_CREDENTIALS.username}</username>
                                <password>${GITHUB_CREDENTIALS.password}</password>
                            </server>
                        </servers>
                    </settings>
                    """
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
