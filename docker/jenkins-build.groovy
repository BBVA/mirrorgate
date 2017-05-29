
def build() {

    if(env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'develop') {
        stage('Docker - build docker image') {
            sh "./build.sh"
        }

        withCredentials([[$class : 'UsernamePasswordMultiBinding',
            credentialsId   : "bot-mirrorgate-dh  ",
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASSWORD']]) {

            stage('Docker - push') {
                TAG = 'latest'
                if(env.BRANCH_NAME == 'master') {
                    TAG = 'release'
                }
                
                sh """
                  ./publish.sh $TAG
                """
            }
        }
    }
}

return this;
