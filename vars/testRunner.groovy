#!/usr/bin/env groovy

/**
 * Librairie partagée pour exécuter des tests
 * Fichier: vars/testRunner.groovy
 * 
 * Contient une fonction réutilisable pour exécuter les tests
 * dans n'importe quel projet (Node.js, Python, Java, etc.)
 */

// Method called when the library is loaded
// This makes the class instantiable from Pipeline scripts
def call() {
    return this
}

/**
 * 2️⃣ Fonction réutilisable pour exécuter les tests
 * 
 * @param command La commande à exécuter (ex: 'pytest tests/', 'npm test', 'mvn test')
 * @param label Label pour identifier les tests (optionnel)
 * @return boolean True si les tests réussissent, False sinon
 */
def executeTests(String command, String label = 'Tests') {
    echo "🧪 [${label}] Exécution de la commande: ${command}"
    
    def exitCode = sh(
        script: """
        ${command}
        """,
        returnStatus: true
    )
    
    if (exitCode != 0) {
        echo "❌ [${label}] Tests échoués (code: ${exitCode})"
        currentBuild.result = 'FAILURE'
    } else {
        echo "✅ [${label}] Tous les tests ont réussi !"
    }
    
    return exitCode == 0
}

/**
 * Fonction pour configurer l'environnement Node.js
 * 
 * @param version Version de Node.js à installer (ex: '20')
 */
def setupNodeEnvironment(String version = '20') {
    echo "🔧 Configuration de Node.js version ${version}..."
    
    sh '''
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
    export NVM_DIR="$HOME/.nvm"
    [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
    nvm install ${version}
    nvm use ${version}
    '''
    
    echo "✅ Node.js ${version} configuré avec succès"
}

/**
 * Fonction pour installer les dépendances npm
 * 
 * @param cleanInstall Utiliser 'npm ci' au lieu de 'npm install' (défaut: true)
 */
def installDependencies(boolean cleanInstall = true) {
    echo "📦 Installation des dépendances npm..."
    
    def command = cleanInstall ? 'npm ci' : 'npm install'
    
    sh '''
    export NVM_DIR="$HOME/.nvm"
    [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
    nvm use 20
    ${command}
    '''
    
    echo "✅ Dépendances npm installées"
}

/**
 * Fonction pour exécuter le linting
 * 
 * @param command Commande de linting (ex: 'npm run lint')
 */
def runLint(String command = 'npm run lint') {
    echo "🔍 Exécution du linting..."
    
    def exitCode = sh(
        script: '''
        export NVM_DIR="$HOME/.nvm"
        [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
        nvm use 20
        ${command}
        ''',
        returnStatus: true
    )
    
    if (exitCode != 0) {
        echo "❌ Linting échoué"
        currentBuild.result = 'FAILURE'
    } else {
        echo "✅ Linting réussi"
    }
    
    return exitCode == 0
}

/**
 * Fonction pour afficher un message de statut
 * 
 * @param message Message à afficher
 * @param isSuccess Si true, affichage en succès, sinon en erreur
 */
def displayStatus(String message, boolean isSuccess = true) {
    def icon = isSuccess ? '✅' : '❌'
    echo "${icon} ${message}"
}

return this
