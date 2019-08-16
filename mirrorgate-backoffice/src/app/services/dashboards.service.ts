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

import { Dashboard } from '../model/dashboard';
import { HttpClient } from '@angular/common/http';

import { ConfigService } from './config.service'

@Injectable()
export class DashboardsService {

  private dashboardsUrl: string;

  constructor(private http: HttpClient, private configService: ConfigService) {
    this.dashboardsUrl = this.configService.getConfig('MIRRORGATE_API_URL') + '/dashboards';
  }

  getDashboards() {
    return this.http.get<Dashboard[]>(this.dashboardsUrl);
  }

  getDashboard(id) {
    return this.http.get<Dashboard>(this.dashboardsUrl + '/' + id + '/details');
  }

  deleteDashboard(dashboard: Dashboard) {
    return this.http.delete(this.dashboardsUrl + '/' + dashboard.name, { responseType: 'text' });
  }

  saveDashboard(dashboard: Dashboard, exists?: boolean) {
    if (exists) {
      return this.http.put<Dashboard>(this.dashboardsUrl + '/' + dashboard.name, dashboard);
    } else {
      return this.http.post<Dashboard>(this.dashboardsUrl, dashboard);
    }
  }

  uploadImage(dashboard: Dashboard, file: File) {
    let formData: FormData = new FormData();
    formData.append('uploadfile', file, file.name);

    return this.http.post(`${this.dashboardsUrl}/${dashboard.name}/image`, formData, { responseType: 'text' });
  }
}
