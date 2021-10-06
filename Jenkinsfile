pipeline {

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        skipDefaultCheckout true
    }

    agent { label 'windows' }

    environment {
        NEXUS_INSTANCE = "Nexus_CloudBees_Guru"
        NEXUS_REPOSITORY = "shared-demos"
        ROLLOUT_APP_TOKEN = "$ROLLOUT_APP_TOKEN"
        ROLLOUT_USER_TOKEN = "$ROLLOUT_USER_TOKEN"
        CALL_SONARQUBE = 'false'
        CALL_CD = 'false'
        CALL_NEXUS = 'false'
        CALL_FF = 'false'
    }

    stages {
        stage('Run maven') {
            steps {
                git(url: 'https://github.com/cloudbees-guru/petclinic', credentialsId: 'github-cloudbees-guru')
                //container('maven') {
                    withMaven(
                            mavenSettingsConfig: '4123d3ce-22c2-477d-83d7-623049473250',
                            options: [junitPublisher(disabled: true, healthScaleFactor: 1.0)],
                            publisherStrategy: 'EXPLICIT') {
                        bat 'mvn clean verify'
                    }
                //}
            }
        }
        stage('SonarQube analysis') {
            when {
                environment name: 'CALL_SONARQUBE', value: 'true'
            }
            steps {
                //container('maven') {
                    withSonarQubeEnv('SonarQube CloudBees Guru') {
                        withMaven(
                                mavenSettingsConfig: '4123d3ce-22c2-477d-83d7-623049473250',
                                options: [junitPublisher(disabled: true, healthScaleFactor: 1.0)],
                                publisherStrategy: 'EXPLICIT') {
                            bat 'mvn sonar:sonar'
                        }
                    }
                //}
            }
        }
        stage('Publish to Nexus') {
            when {
                environment name: 'CALL_NEXUS', value: 'true'
            }
            steps {
                //container('maven') {
                    script {
                        pom = readMavenPom file: "pom.xml";
                        filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                        // Extract the path from the File found
                        artifactPath = filesByGlob[0].path;
                        // Assign to a boolean response verifying If the artifact name exists
                        artifactExists = fileExists artifactPath;

                        if (artifactExists) {
                            nexusPublisher nexusInstanceId: NEXUS_INSTANCE,
                                    nexusRepositoryId: NEXUS_REPOSITORY,
                                    packages: [[$class         : 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: artifactPath]],
                                                mavenCoordinate: [artifactId: pom.artifactId, groupId: pom.groupId, packaging: pom.packaging, version: pom.version]]];
                        } else {
                            error "*** File: ${artifactPath}, could not be found";
                        }
                    }
                //}
            }
        }
        stage('Feature flag usage check') {
            when {
                environment name: 'CALL_FF', value: 'true'
            }
            steps {
                //container('maven') {
                    script {
                        sh """
               curl -o file.json \"https://x-api.rollout.io/public-api/applications/${ROLLOUT_APP_TOKEN}/Production/experiments" -H "accept: application/json" -H "Authorization: Bearer ${ROLLOUT_USER_TOKEN}"''
               cat file.json | sed -e 's/},/},\\n/g' > file.json.new
              """
                        ALLEXP = sh(
                                script: 'cat file.json.new | grep value | wc -l',
                                returnStdout: true
                        ).trim()
                        echo "All experiments: ${ALLEXP}"
                        KILLEDEXP = sh(
                                script: 'cat file.json.new | grep enabled.*false | wc -l',
                                returnStdout: true
                        ).trim()
                        echo "Inactive experiments: ${KILLEDEXP}"
                    }
                //}
            }
        }
        stage('Trigger CloudBees CD pipeline') {
            when {
                environment name: 'CALL_CD', value: 'true'
            }
            steps {
                cloudBeesFlowRunPipeline addParam: '{"pipeline":{"pipelineName":"spring-petclinic - Demo pipeline","parameters":"[{\\"parameterName\\": \\"applicationVersion\\", \\"parameterValue\\": \\"2.4.5\\"}]"}}', configuration: 'CloudBees Guru CD', pipelineName: 'spring-petclinic - Demo pipeline', projectName: 'Shared demos'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
        }
    }
    //
}
