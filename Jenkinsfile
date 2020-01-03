pipeline {
   agent any
   stages {
      stage('Build') {
         steps {
            sh '''
               mvn clean && npm --prefix src/main/frontend run build && mvn package
            '''
         }
      }
      stage('Archive') {
         steps {
            script {
                export COMMIT_ID=`cat .git/HEAD`
                sudo cp ${WORKSPACE}/target/devops*.jar ${WORKSPACE}/target/${COMMIT_ID}.jar               
                def remote = [:]
                remote.name = 'archiver'
                remote.user = 'vagrant'
                remote.allowAnyHosts = true
                remote.host = 'archiver.local'
                remote.identityFile = '~/.ssh/archiver.key'
                    sshPut remote: remote, from: '${COMMIT_ID}.jar', into: '/home/vagrant/'
            }
         }
      }   
   }
}