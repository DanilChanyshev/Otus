node('ui_runner') {

    currentBuild.description = "<p style='color: blue;'>UI tests</p>"

    stage('Checkout') {
        checkout scm
    }

    stage('Run UI tests') {
        dir("${env.WORKSPACE}") {
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                sh "ansible-playbook -i playbook/inventory/hosts playbook/run_ui_test.yaml -e \"workspace=\$(pwd)\""
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
                results: [[path: 'target/allure-results']]
        ])
    }

}