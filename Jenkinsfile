pipeline {
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
    image: maven:3.6.2-jdk-8-slim
    command:
    - cat
    tty: true
"""
    }
  }
  environment {
        SONAR_TOKEN = credentials('sonar.login')
    }
  stages {
    stage('Run maven') {
      steps {
          git(url:'https://github.com/jpbriend/spring-petclinic', credentialsId: 'github')
        container('maven') {
          sh 'mvn verify sonar:sonar -Dsonar.login=${SONAR_TOKEN}'
        }
      }
    }
  }
  
  post {
    always {
      junit 'target/surefire-reports/**/*.xml'
    }
  }
}
