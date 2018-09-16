@Library('Utilities2') _
node ('worker_node1') {
    
    stage('Source'){
        cleanWs()
        // Get code from our git repo
        git 'git@diyvb2:/home/git/repositories/workshop.git'
        stash includes: 'api/**, dataaccess/**, util/**, build.gradle, settings.gradle'
    }
    
    stage('Compile'){
        // Run gradle to execute compile

      gbuild4 "clean compileJava -x test"        
    }
    
    stage('Unit Test'){
        
        parallel(
            
            tester1: { node ('worker_node2') {
                // always run with a new workspace
               cleanWs()
               unstash 'ws-src'
	       gbuild4 ':util:test'
            }},
            
            tester2: { node ('worker_node3'){
               // always run with a new workspace
               cleanWs()
               unstash 'ws-src'
	       gbuild4 '-D test.single=TestExample1* :api:test'
            }},
            
            tester3: { node ('worker_node2'){
                // always run with a new workspace
                cleanWs()
                unstash 'ws-src'
  	        gbuild4 '-D test.single=TestExample2* :api:test'
            }},
            
            )    
    }
    
    stage('Integration Test'){
        // set up integration database
        sh "mysql -uadmin -padmin registry_test < registry_test.sql"
        gbuild4 'integrationTest'
    }

    stage('')
}