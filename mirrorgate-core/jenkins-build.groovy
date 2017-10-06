/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

def build() {

    withCredentials([[$class: 'FileBinding', credentialsId: 'artifactory-maven-settings-global', variable: 'M3_SETTINGS']]) {
        sh 'mkdir .m3 || true'
        sh 'cp -f ${M3_SETTINGS} .m3/settings.xml'
    }

    stage('Core - Build jar') {
        sh """
            ./gradlew clean build
        """
    }

    
    if(env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'develop' && env.SPINUP_JOB != 'true') {

        stage(' Publish app ') {
            withCredentials([[$class          : 'UsernamePasswordMultiBinding',
                              credentialsId   : "bot-mirrorgate-st",
                              usernameVariable: 'mavenUser',
                              passwordVariable: 'mavenPassword']]) {

                sh "./gradlew uploadArchives"

            }

        }
    }

}

return this;
