#!/usr/bin/env groovy

/**
 * Librairie partagée pour les pipelines Jenkins
 * Fichier: vars/monPipeline.groovy
 * Contient des fonctions réutilisables pour exécuter les tests
 */

def call() {
    // Permet d'appeler le fichier directement depuis Jenkins
    return this
}

/**
 * Fonction pour configurer l'environnement Python
 * @param version Version de Python à installer (ex: '3.9')
 */
def setupPythonEnvironment(String version = '3.9') {
    echo "🔧 Configuration de Python version ${version}..."
    
    sh """
    # Installation de Python via pyenv ou apt
    if ! command -v python${version} &> /dev/null; then
        echo "Python ${version} non trouvé, installation..."
        apt-get update
        apt-get install -y software-properties-common
        add-apt-repository -y ppa:deadsnakes/ppa
        apt-get update
        apt-get install -y python${version} python${version}-dev python${version}-pip
    fi
    
    # Configuration de pip
    curl -sS https://bootstrap.pypa.io/get-pip.py | python${version}
    python${version} -m pip install --upgrade pip
    echo "Python ${version} installé avec succès"
    """
    
    echo "✅ Python ${version} configuré avec succès"
}

/**
 * Fonction pour installer les dépendances Python
 * @param requirementsFile Fichier requirements.txt (défaut: 'requirements.txt')
 */
def installPythonDependencies(String requirementsFile = 'requirements.txt') {
    echo "📦 Installation des dépendances Python..."
    
    def exitCode = sh(
        script: """
        if [ -f ${requirementsFile} ]; then
            pip install -r ${requirementsFile}
        else
            echo "Fichier ${requirementsFile} non trouvé, installation de base..."
            pip install pytest
        fi
        """,
        returnStatus: true
    )
    
    if (exitCode != 0) {
        echo "❌ Échec de l'installation des dépendances"
        currentBuild.result = 'FAILURE'
    } else {
        echo "✅ Dépendances Python installées"
    }
    
    return exitCode == 0
}

/**
 * 2️⃣ Fonction réutilisable pour exécuter les tests
 * @param testCommand Commande de test à exécuter (ex: 'pytest tests/')
 */
def executeTests(String testCommand) {
    echo "🧪 Exécution des tests: ${testCommand}..."
    
    def exitCode = sh(
        script: """
        ${testCommand}
        """,
        returnStatus: true
    )
    
    if (exitCode != 0) {
        echo "❌ Tests échoués"
        currentBuild.result = 'FAILURE'
    } else {
        echo "✅ Tous les tests ont réussi !"
    }
    
    return exitCode == 0
}

/**
 * Fonction alternative pour exécuter pytest
 * @param testPath Chemin vers les tests (ex: 'tests/')
 */
def runPytest(String testPath = 'tests/') {
    echo "🧪 Exécution de pytest sur: ${testPath}"
    
    def exitCode = sh(
        script: """
        pytest ${testPath} -v --tb=short
        """,
        returnStatus: true
    )
    
    if (exitCode != 0) {
        echo "❌ Tests pytest échoués"
        currentBuild.result = 'FAILURE'
    } else {
        echo "✅ Tests pytest réussis"
    }
    
    return exitCode == 0
}

/**
 * Fonction pour générer un rapport de couverture
 * @param coverageCommand Commande de coverage (ex: 'pytest --cov=src tests/')
 */
def generateCoverageReport(String coverageCommand) {
    echo "📊 Génération du rapport de couverture..."
    
    def exitCode = sh(
        script: """
        pip install pytest-cov
        ${coverageCommand}
        """,
        returnStatus: true
    )
    
    if (exitCode != 0) {
        echo "❌ Génération du rapport de couverture échouée"
        currentBuild.result = 'FAILURE'
    } else {
        echo "✅ Rapport de couverture généré"
    }
    
    return exitCode == 0
}

/**
 * Fonction pour afficher un message de statut
 * @param message Message à afficher
 * @param isSuccess Si true, affichage en succès, sinon en erreur
 */
def displayStatus(String message, boolean isSuccess = true) {
    def icon = isSuccess ? '✅' : '❌'
    echo "${icon} ${message}"
}

return this
