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
               mvn -f backend/pom.xml clean package
               npm --prefix frontend install
            '''   
         }
      }
      stage('Archive') {
         steps {
            
            sh '''
                  tar czf ${GIT_COMMIT}.tar.gz frontend/
                  mv ${WORKSPACE}/backend/target/*.jar ${WORKSPACE}/backend/target/${GIT_COMMIT}.jar
               '''            
            script {
                def remote = [:]
                remote.name = 'archiver'
                remote.user = 'vagrant'
                remote.allowAnyHosts = true
                remote.host = 'archiver.local'
                remote.identityFile = '~/.ssh/archiver.key'
                sshPut remote: remote, filterRegex: '.jar$',from: './backend/target' ,into: '/home/vagrant/archiver/backend'
                sshPut remote: remote, filterRegex: '.tar.gz$',from: '.' ,into: '/home/vagrant/archiver/frontend'
            }
         }
      }
      stage('Staging Setup') {
      steps {
               parallel(
                  app: { // Prepare the Docker image for the staging ui
                        sh '''
                              mv frontend/nginx-staging.conf frontend/nginx.conf
                              docker build --build-arg STAGE=staging -t "devops/ui:staging" -f frontend/Dockerfile .
                              docker tag "devops/ui:staging" "${REGISTRY}/devops/ui:staging"
                              docker push "${REGISTRY}/devops/ui:staging"
                              docker rmi "${REGISTRY}/devops/ui:staging"
    
                              docker build --build-arg FILE_NAME=${GIT_COMMIT} -t "devops/api:staging" -f backend/Dockerfile .
                              docker tag "devops/api:staging" "${REGISTRY}/devops/api:staging"
                              docker push "${REGISTRY}/devops/api:staging"
                              docker rmi "${REGISTRY}/devops/api:staging"
                           '''
                  },
                  db: { // Parallely start the MySQL Daemon in the staging server first stop if already running then start
                        script {
                           def remote = [:]
                           remote.name = 'staging'
                           remote.user = 'vagrant'
                           remote.allowAnyHosts = true
                           remote.host = 'staging.local'
                           remote.identityFile = '~/.ssh/staging.key'
                           sshCommand remote: remote, command: "docker stop mysqldb backend frontend || true"
                           sshCommand remote: remote, command: "docker rm mysqldb backend frontend || true"
                           sshCommand remote: remote, command: "docker run -d -p 3306:3306 \
                           -e MYSQL_DATABASE=test -e MYSQL_ROOT_PASSWORD=tooor -e MYSQL_USER=test -e MYSQL_PASSWORD=test \
                            -v /home/vagrant/mysql:/var/lib/mysql \
                            --name mysqldb mysql \
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
                sshCommand remote: remote, command: "docker run -d -p 8080:8080 --link mysqldb \
                  --name backend ${REGISTRY}/devops/devops/api:staging"
                sshCommand remote: remote, command: "docker run -d -p 80:80 --link backend \
                  --name frontend ${REGISTRY}/devops/ui:staging"                  
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