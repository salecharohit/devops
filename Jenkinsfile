pipeline {
   agent any
   stages {
      stage('SCM') {
         steps {
             checkout scm 
         }
      }
      stage('Build') {
         steps {
            sh '''
                node --version
                mvn --version
            '''
         }
      }   
   }
}