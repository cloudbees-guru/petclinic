pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
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
    image: maven:3.6.2-jdk-8-slim
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
    SONAR_TOKEN = credentials('sonar.login')
    NEXUS_VERSION = "nexus3"
    NEXUS_PROTOCOL = "https"
    NEXUS_URL = "nexus.cloudbees.guru:8081"
    NEXUS_REPOSITORY = "shared-demos"
    NEXUS_CREDENTIAL_ID = "nexus"
    ROLLOUT_APP_TOKEN = "$ROLLOUT_APP_TOKEN"
    ROLLOUT_USER_TOKEN = "$ROLLOUT_USER_TOKEN"
    }
  stages {
    stage('Run maven') {
      steps {
          git(url:'https://github.com/cloudbees-guru/petclinic', credentialsId: 'github-cloudbees-guru')
          container('maven') {
            withMaven(
                      mavenSettingsConfig: '8b13860a-f881-47c0-81bf-4192e70fc34d') {
              sh 'echo $ROLLOUT_APP_TOKEN'
              sh 'echo $ROLLOUT_USER_TOKEN'
              sh 'curl -o file.json  "https://x-api.rollout.io/public-api/applications/${ROLLOUT_APP_TOKEN}/Production/experiments" -H 'accept: application/json' -H "Authorization: Bearer ${ROLLOUT_USER_TOKEN}"''
              sh 'cat file.json'
              sh 'cat file.json | sed -e "s/},/},\n/g" > file.json.new'
              sh 'cat file.json.new'
              sh 'cat file.json.new | grep value.*false | wc -l'
              sh 'cat file.json.new | grep value.*true | wc -l'
              sh """
               curl -o file.json \"https://x-api.rollout.io/public-api/applications/${ROLLOUT_APP_TOKEN}/Production/experiments" -H "accept: application/json" -H "Authorization: Bearer ${ROLLOUT_USER_TOKEN}"''
               cat file.json | sed -e "s/},/},\n/g" > file.json.new
               ALLEXP=`cat file.json.new | grep value | wc -l`
               INACTIVEEXP=`cat file.json.new | grep value.*false | wc -l`
               ACTIVEEXP=`cat file.json.new | grep value.*true | wc -l`
               echo "***************"
               echo experiment count: $ALLEXP
               echo active $ACTIVEEXP
               echo inactive $INACTIVEEXP
               echo "***************"
              """
            }
          }
      }
    }
  }
}
