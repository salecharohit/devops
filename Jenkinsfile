def COMMIT_ID
pipeline {
   agent any
   stages {
      stage('Build') {
         steps {
            sh '''
               mvn clean
               npm --prefix src/main/frontend install
               npm --prefix src/main/frontend run build
               mvn package
            '''
         }
      }
      stage('Archive') {
         steps {
            script{
               COMMIT_ID = sh (
                     script: "cat .git/HEAD",
                     returnStdout: true
               ).trim()
            }         
            sh '''
                  // export COMMIT_ID=`cat .git/HEAD`
                  mv ${WORKSPACE}/target/*.jar ${WORKSPACE}/target/${COMMIT_ID}.jar
               '''            
            script {
                def remote = [:]
                remote.name = 'archiver'
                remote.user = 'vagrant'
                remote.allowAnyHosts = true
                remote.host = 'archiver.local'
                remote.identityFile = '~/.ssh/archiver.key'
                    sshPut remote: remote, filterRegex: '.jar$',from: './target' ,into: '/home/vagrant/archiver'
            }
         }
      }
      stage('Docker Build') {
      steps {
         parallel(
            app: {
               sh '''
                     
                     docker build --build-arg user=what_user .
                  '''
            },
            db: {
               echo "This is branch b"
            }
         )
      }
      }         
   }
    post {
    failure {
      script {
        currentBuild.result = 'FAILURE'
      }
    }
    always {
          step([$class: 'Mailer',
          notifyEveryUnstableBuild: true,
          recipients: "build-failed@devops.local",
          sendToIndividuals: true])
    }
  }
}