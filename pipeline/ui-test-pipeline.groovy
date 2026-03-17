node('ui_runner') {

    currentBuild.description = "<p style='color: blue;'>API tests</p>"

    stage('Checkout') {
        checkout scm
    }

    stage('Run UI tests') {
        dir("${env.WORKSPACE}") {
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                sh "mvn clean test"
            }
        }
    }

    stage('Publish results') {
        junit 'target/surefire-reports/*.xml'
    }

    stage('allure publish') {
        allure ([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'allure-results']]
        ])
    }

}