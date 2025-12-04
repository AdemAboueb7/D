pipeline { 
    agent any
    
    environment {
        M2_HOME = "/usr/share/maven"
        IMAGE_NAME = "ademab/alyou"
        DOCKERHUB_CREDENTIALS = "dockerhub-cred"
    }
    
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/AdemAboueb7/D.git', branch: 'main'
            }
        }
        
        stage('Test') {
            steps {
                bat "wsl mvn -q test -Dspring.profiles.active=test"
            }
        }
        
        
        
        stage('Package') {
            steps {
                bat "wsl mvn -q package -Dspring.profiles.active=test"
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo "Checking Docker..."
                bat "wsl docker --version"
                echo "Building image ${IMAGE_NAME}:latest"
                bat """
                    wsl docker build -t ${IMAGE_NAME}:latest .
                """
            }
        }
        
        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: DOCKERHUB_CREDENTIALS,
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    echo "Logging to Docker Hub..."
                    bat """
                        echo %DOCKER_PASS% | wsl docker login -u %DOCKER_USER% --password-stdin
                    """
                    echo "Pushing Docker image..."
                    bat "wsl docker push ${IMAGE_NAME}:latest || exit 0"
                }
            }
        }
       stage('MVN SONARQUBE') {
    steps {
        echo "Analyzing code quality with SonarQube..."
        script {
            withSonarQubeEnv('SonarQube') {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    bat """
                        wsl mvn clean verify sonar:sonar ^
                        -Dsonar.projectKey=alyou-project ^
                        -Dsonar.projectName="Alyou Application" ^
                        -Dsonar.host.url=http://localhost:9000 ^
                        -Dsonar.token=%SONAR_TOKEN% ^
                        -Dspring.profiles.active=test
                    """
                }
            }
        }
    }
}
    }
    
    post {
        always {
            echo "Pipeline finished - cleaning..."
            bat '''
                wsl bash -c "docker logout" || echo "logout ignored"
            '''
        }
        success {
            echo "Build succeeded! Image pushed to DockerHub"
        }
        failure {
            echo "Build failed!"
        }
    }
}