pipeline {
   agent any
   environment {
     BUILD_VERSION = ""
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
                  export BUILD_VERSION = ${env.BUILD_NUMBER}_${GIT_COMMIT}
                  mv ${WORKSPACE}/target/*.jar ${WORKSPACE}/target/${BUILD_VERSION}.jar
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
                        export BUILD_VERSION = ${env.BUILD_NUMBER}_${GIT_COMMIT}
                        docker build devops/app -f APP.Dockerfile --build-arg file_name="${BUILD_VERSION}" .
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