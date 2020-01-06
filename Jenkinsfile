def app
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
            sh '''
                  mv ${WORKSPACE}/target/*.jar ${WORKSPACE}/target/${GIT_COMMIT}.jar
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
               app = docker.build("devops/app","file_name=${GIT_COMMIT}")
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