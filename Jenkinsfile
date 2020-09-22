pipeline {
  agent { docker { image 'python:3.7.9' } }
  stages {
    stage('Build') {
      steps {
        sh 'python --version'
        sh 'python -m pip install --user -r requirements.txt'
        sh 'python app.py'
        stash(name: 'compiled-results', includes: '*.py*')
        echo 'Building'
      }
    }
    stage('Test') {
      steps {
        sh 'python test.py'
        echo 'Testing'
      }
    }
    stage('Deploy') {
            steps {
                echo 'Deploying'
            }
        }
  }
      post {
        always {
          junit 'test-reports/*.xml'
          echo 'this thing always runs'
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
      }    
    }
