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
    NEXUS_VERSION = "nexus3"
    NEXUS_PROTOCOL = "https"
    NEXUS_URL = "nexus.cloudbees.guru:8081"
    NEXUS_REPOSITORY = "shared-demos"
    NEXUS_CREDENTIAL_ID = "nexus"
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
                nexusArtifactUploader(
                  nexusVersion: NEXUS_VERSION,
                  protocol: NEXUS_PROTOCOL,
                  nexusUrl: NEXUS_URL,
                  groupId: pom.groupId,
                  version: pom.version,
                  repository: NEXUS_REPOSITORY,
                  credentialsId: NEXUS_CREDENTIAL_ID,
                  artifacts: [
                    // Artifact generated such as .jar, .ear and .war files.
                    [artifactId: pom.artifactId,
                      classifier: '',
                      file: artifactPath,
                      type: pom.packaging],
                      // Lets upload the pom.xml file for additional information for Transitive dependencies
                      [artifactId: pom.artifactId,
                        classifier: '',
                        file: "pom.xml",
                        type: "pom"]
                    ]
                  );
               } else {
                 error "*** File: ${artifactPath}, could not be found";
               }
            }
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
