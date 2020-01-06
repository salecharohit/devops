pipeline {
   agent any
   environment {
     APP = ""
     REGISTRY = "registry.local:5000"
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
                        docker build --build-arg FILE_NAME=${GIT_COMMIT} -t "devops/app:${BUILD_NUMBER}" -f APP.Dockerfile .
                        docker tag "devops/app:${BUILD_NUMBER}" "${REGISTRY}/devops/app:${BUILD_NUMBER}"
                        docker push "${REGISTRY}/devops/app:${BUILD_NUMBER}"
                        docker rmi "devops/app:${BUILD_NUMBER}"
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