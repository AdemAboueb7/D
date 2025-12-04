pipeline {
    agent any

    tools {
        maven 'Maven_3_9'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "📥 Récupération du code source..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "🔨 Compilation du projet..."
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                echo "🧪 Exécution des tests unitaires..."
                sh 'mvn test'
            }
        }

        stage('Code Coverage (JaCoCo)') {
            steps {
                echo "📊 Génération du rapport JaCoCo..."
                sh 'mvn jacoco:report'
            }
        }

        stage('Package') {
            steps {
                echo "📦 Empaquetage de l'application..."
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            echo "🏁 Pipeline terminée"
            junit 'target/surefire-reports/**/*.xml'
            publishHTML([
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'JaCoCo Coverage Report'
            ])
        }
        success {
            echo '✅ Build réussi avec tous les tests passés!'
        }
        failure {
            echo '❌ Build échoué - vérifiez les logs des tests.'
        }
    }
}
