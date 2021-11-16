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
    stage('Publish to CloudBees CD') {
      steps {
          container('maven') {
            script {
              sh 'find / -type f -name "petclinic*"'
              cloudBeesFlowPublishArtifact artifactName: 'pcherry:petclinic-pcherry',
                                           artifactVersion: '1.0',
                                           configuration: 'CloudBees Guru CD',
                                           filePath: 'target/*',
                                            repositoryName: 'default'
               }
            }
          }
      }
  }

}
