pipeline {

  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    skipDefaultCheckout true
  }

  agent {
    kubernetes {
      yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: some-label-value
spec:
  containers:
  - name: maven
    image: maven:3.8.1-jdk-11-slim
    command:
    - cat
    tty: true
    volumeMounts:
      - name: cache
        mountPath: /tmp/cache
  volumes:
    - name: cache
      hostPath:
        path: /tmp/cache
"""
    }
  }

  environment {
    NEXUS_INSTANCE = "Nexus_CloudBees_Guru"
    NEXUS_REPOSITORY = "shared-demos"
    ROLLOUT_APP_TOKEN = "$ROLLOUT_APP_TOKEN"
    ROLLOUT_USER_TOKEN = "$ROLLOUT_USER_TOKEN"
  }

  stages {
    stage('Run maven') {
      steps {
          git(url:'https://github.com/cloudbees-guru/petclinic', credentialsId: 'github-cloudbees-guru')
          container('maven') {
            withMaven(
                      mavenSettingsConfig: '4123d3ce-22c2-477d-83d7-623049473250',
                      options: [junitPublisher(disabled: true, healthScaleFactor: 1.0)],
                      publisherStrategy: 'EXPLICIT') {
              sh 'mvn clean verify'
            }
          }
      }
    }
    stage('SonarQube analysis') {
      steps {
          container('maven') {
            withSonarQubeEnv('SonarQube CloudBees Guru') {
              withMaven(
                        mavenSettingsConfig: '4123d3ce-22c2-477d-83d7-623049473250',
                        options: [junitPublisher(disabled: true, healthScaleFactor: 1.0)],
                        publisherStrategy: 'EXPLICIT') {
                sh 'mvn sonar:sonar'
              }
            }
          }
      }
    }
    stage('Publish to Nexus') {
      steps {
          container('maven') {
            script {
              pom = readMavenPom file: "pom.xml";
              filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
              // Extract the path from the File found
              artifactPath = filesByGlob[0].path;
              // Assign to a boolean response verifying If the artifact name exists
              artifactExists = fileExists artifactPath;

              if(artifactExists) {
                nexusPublisher  nexusInstanceId: NEXUS_INSTANCE,
                                nexusRepositoryId: NEXUS_REPOSITORY,
                                packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: artifactPath]],
                                mavenCoordinate: [artifactId: pom.artifactId, groupId: pom.groupId, packaging: pom.packaging, version: pom.version]]];
               } else {
                 error "*** File: ${artifactPath}, could not be found";
               }
            }
          }
      }
    }
    stage('Feature flag usage check') {
      steps {
          container('maven') {
            script {
              sh """
               curl -o file.json \"https://x-api.rollout.io/public-api/applications/${ROLLOUT_APP_TOKEN}/Production/experiments" -H "accept: application/json" -H "Authorization: Bearer ${ROLLOUT_USER_TOKEN}"''
               cat file.json | sed -e 's/},/},\\n/g' > file.json.new
              """
              ALLEXP = sh (
                  script: 'cat file.json.new | grep value | wc -l',
                  returnStdout: true
              ).trim()
              echo "All experiments: ${ALLEXP}"
              KILLEDEXP = sh (
                  script: 'cat file.json.new | grep enabled.*false | wc -l',
                  returnStdout: true
              ).trim()
              echo "Inactive experiments: ${KILLEDEXP}"
            }
          }
      }
    }
    stage('Trigger CloudBees CD pipeline') {
      steps {
        cloudBeesFlowRunPipeline addParam: '{"pipeline":{"pipelineName":"spring-petclinic - Demo pipeline","parameters":[]}}', configuration: 'CloudBees Guru CD', pipelineName: 'spring-petclinic - Demo pipeline', projectName: 'Shared demos'
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/**/*.xml'
    }
  }
}
