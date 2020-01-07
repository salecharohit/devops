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
      stage('Staging Setup') {
      steps {
               parallel(
                  app: { // Prepare the Docker image for the staging app
                        sh '''
                              docker build --build-arg FILE_NAME=${GIT_COMMIT} -t "devops/app:${BUILD_NUMBER}" -f APP.Dockerfile .
                              docker tag "devops/app:${BUILD_NUMBER}" "${REGISTRY}/devops/app:${BUILD_NUMBER}"
                              docker push "${REGISTRY}/devops/app:${BUILD_NUMBER}"
                              docker rmi "${REGISTRY}/devops/app:${BUILD_NUMBER}"
                           '''
                  },
                  db: { // Parallely start the MySQL Daemon in the staging server
                        script {
                           def remote = [:]
                           remote.name = 'staging'
                           remote.user = 'vagrant'
                           remote.allowAnyHosts = true
                           remote.host = 'staging.local'
                           remote.identityFile = '~/.ssh/staging.key'
                           sshCommand remote: remote, command: "docker run -d -p 3306:3306 \
                           -e MYSQL_DATABASE=test -e MYSQL_ROOT_PASSWORD=tooor -e MYSQL_USER=test -e MYSQL_PASSWORD=test -v /home/vagrant/mysql:/var/lib/mysql --name mysqldb mysql \
                           --default-authentication-plugin=mysql_native_password"
                        }               
                  }
               )
            }
      }
      stage('Staging Deploy') {
         steps {   
            script {
                def remote = [:]
                remote.name = 'staging'
                remote.user = 'vagrant'
                remote.allowAnyHosts = true
                remote.host = 'staging.local'
                remote.identityFile = '~/.ssh/staging.key'
                sshCommand remote: remote, command: "docker run -d -p 80:8080 --link mysqldb ${REGISTRY}/devops/app:${BUILD_NUMBER}"
            }
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