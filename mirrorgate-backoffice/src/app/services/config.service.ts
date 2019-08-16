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

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ConfigService {
  private static config: Object;

  constructor(private http: HttpClient) { }

  getConfig(key: string) {
    return ConfigService.config[key];
  }

  loadConfig() {
    return new Promise((resolve, reject) => {
      this.http.get('config.json').subscribe(
        config => {
          ConfigService.config = config;
          resolve(true);
        },
        error => {
          console.error(error);
          reject(error);
        }
      );
    });
  }
}
