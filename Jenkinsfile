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
               ls -al
            '''
         }
      }
      stage('Archive') {
         steps {
         sh '''
               export COMMIT_ID=`cat .git/HEAD`
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
   }
}