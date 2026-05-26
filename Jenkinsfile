#!/usr/bin/env groovy

/**
 * Jenkinsfile pour le projet shared-lib-jenkins
 * Pipeline CI/CD automatisée avec:
 * - Variables d'environnement
 * - Librairie partagée (vars/monPipeline.groovy)
 * - Exécution de tests Python
 */

pipeline {
    agent any

    // ============================================
    // 2️⃣ VARIABLES D'ENVIRONNEMENT (les "vars")
    // ============================================
    environment {
        // Configuration Python
        PYTHON_VERSION = '3.9'
        
        // Configuration du projet
        PROJECT_NAME = 'shared-lib-jenkins'
        
        // Commandes de test
        TEST_COMMAND = 'python -m pytest tests/ -v'
        COVERAGE_COMMAND = 'python -m pytest tests/ --cov=src --cov-report=html'
        
        // Chemins
        SOURCE_PATH = 'src/'
        TEST_PATH = 'tests/'
        COVERAGE_PATH = 'htmlcov/**'
    }

    stages {
        // ============================================
        // ÉTAPE 1 : Récupération du code
        // ============================================
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    def monPipeline = load 'vars/monPipeline.groovy'
                    monPipeline.displayStatus("Code récupéré depuis le repository Git")
                }
            }
        }

        // ============================================
        // ÉTAPE 2 : Configuration de l'environnement Python
        // ============================================
        stage('Setup Python') {
            steps {
                script {
                    def monPipeline = load 'vars/monPipeline.groovy'
                    // Utilisation de la variable PYTHON_VERSION
                    monPipeline.setupPythonEnvironment(env.PYTHON_VERSION)
                }
            }
        }

        // ============================================
        // ÉTAPE 3 : Installation des dépendances
        // ============================================
        stage('Install dependencies') {
            steps {
                script {
                    def monPipeline = load 'vars/monPipeline.groovy'
                    monPipeline.installPythonDependencies()
                }
            }
        }

        // ============================================
        // ÉTAPE 4 : Exécution des tests
        // ============================================
        stage('Run Tests') {
            steps {
                script {
                    def monPipeline = load 'vars/monPipeline.groovy'
                    // Utilisation de la variable TEST_COMMAND
                    def testsSuccess = monPipeline.executeTests(env.TEST_COMMAND)
                    
                    if (!testsSuccess) {
                        error("Tests échoués - corrigez les erreurs")
                    }
                }
            }
        }

        // ============================================
        // ÉTAPE 5 : Génération du rapport de couverture
        // ============================================
        stage('Generate Coverage Report') {
            steps {
                script {
                    def monPipeline = load 'vars/monPipeline.groovy'
                    // Utilisation de la variable COVERAGE_COMMAND
                    monPipeline.generateCoverageReport(env.COVERAGE_COMMAND)
                }
            }
        }
    }

    // ============================================
    // POST-ACTIONS
    // ============================================
    post {
        always {
            script {
                def monPipeline = load 'vars/monPipeline.groovy'
                // Archivage du rapport de couverture
                archiveArtifacts artifacts: env.COVERAGE_PATH, allowEmptyArchive: true
            }
        }
        success {
            script {
                def monPipeline = load 'vars/monPipeline.groovy'
                monPipeline.displayStatus("✅ Build réussie pour ${env.PROJECT_NAME}!")
            }
        }
        failure {
            script {
                def monPipeline = load 'vars/monPipeline.groovy'
                monPipeline.displayStatus("❌ Build échouée pour ${env.PROJECT_NAME}!", false)
            }
        }
    }
}
