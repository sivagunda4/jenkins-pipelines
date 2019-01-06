pipeline {
  agent any
  stages {
    stage('FluffyBuild') {
      steps {
        sh './jenkins/build.sh'
      }
    }
    stage('FluffyTest') {
      steps {
        sh './jenkins/test-all.sh'
      }
    }
    stage('FluffyDeploy') {
      steps {
        sh './jenkins/deploy.sh staging'
      }
    }
  }
}