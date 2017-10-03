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

  try {

      stage('Dashboard - Install dependencies') {
          sh """
              docker-compose -p \${BUILD_TAG} run -u \$(id -u) install
          """
      }

      stage('Dashboard - Build app') {
          sh """
            docker-compose -p \${BUILD_TAG} run -e GIT_BRANCH=\${env.BRANCH_NAME} -u \$(id -u) build
          """
      }

      stage('Dashboard - Run tests') {
          sh """
              docker-compose -p \${BUILD_TAG} run -u \$(id -u) test
          """
      }

  } finally {
      sh """
          docker-compose -p \${BUILD_TAG} down --volumes
      """
  }

}

return this;
