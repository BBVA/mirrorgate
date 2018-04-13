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

modules = [
  'mirrorgate-api',
  'mirrorgate-dashboard',
  'mirrorgate-backoffice',
  'mirrorgate-docs',
  'tests',
  'docker',
]

node ('global') {

  wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm', 'defaultFg': 1, 'defaultBg': 2]) {
    withEnv(['CI=true']) {

      try {

        stage('Checkout') {
            checkout(scm)
        }

        for (module in modules) {
          dir(module) {
            fileLoader.load('jenkins-build').build();
          }
        }

      } catch(Exception e) {
        throw e;
      }
    }
  }
}
