pipeline {
   agent any
   environment {
     APP = ""
     registry = "registry.local:5000/devops"
   }
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
      stage('Setup Staging') {
      steps {
         parallel(
            app: {
                  sh '''
                        docker build --build-arg file_name=${GIT_COMMIT} -t "${registry}:${BUILD_NUMBER}" -f APP.Dockerfile .
                        
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