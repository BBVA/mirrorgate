
def build() {

    if(env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'develop' || env.SPINUP_JOB == 'true') {
        stage('Docker - build docker image') {
            sh "./build.sh"
        }

        if(env.SPINUP_JOB == 'true') {
            stage('Docker - tag docker image for spinup on demand'){
                TAG = "${env.SPINUP_BRANCH_LC}"
                sh "docker tag mirrorgate bbvaae/mirrorgate:${TAG}"
            }
        } else {
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
}

return this;
