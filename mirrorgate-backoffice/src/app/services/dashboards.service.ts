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
import { Http } from '@angular/http';
import { Headers, RequestOptions, Response } from '@angular/http';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/filter';

@Injectable()
export class DashboardsService {

  private dashboardsUrl = '../dashboards';
  private dashboardsAPI = '../api/dashboards';

  constructor(private http: Http) { }

  getDashboards(): Promise<Dashboard[]> {
    return this.http.get(this.dashboardsUrl)
               .toPromise()
               .then(response => response.json() as Dashboard[])
               .catch(this.handleError);
  }

  getDashboard(id): Promise<Dashboard> {
    return this.http.get(this.dashboardsUrl + '/' + id + '/details')
               .toPromise()
               .then(response => response.json() as Dashboard)
               .catch(this.handleError);
  }

  deleteDashboard(dashboard: Dashboard): Promise<Boolean> {
    return this.http.delete(this.dashboardsUrl + '/' + dashboard.name)
               .toPromise()
               .then(response => response.status === 200)
               .catch(this.handleError);
  }

  saveDashboard(dashboard: Dashboard, exists?:boolean): Promise<Dashboard> {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    if(exists) {
      return this.http.put(this.dashboardsUrl + '/' + dashboard.name, dashboard, options)
              .toPromise()
              .then(response => response.json() as Dashboard)
              .catch(this.handleError);
    } else {
      return this.http.post(this.dashboardsUrl, dashboard, options)
                .toPromise()
                .then(response => response.json() as Dashboard)
                .catch(this.handleError);
    }
  }

  uploadImage(dashboard: Dashboard, file: File): Promise<any>{
    let formData:FormData = new FormData();
    formData.append('uploadfile', file, file.name);
    let headers = new Headers();
    //headers.append('Content-Type', null);

    let options = new RequestOptions({ headers: headers });
    return this.http.post(`${this.dashboardsUrl}/${dashboard.name}/image`, formData, options)
        .toPromise()
        .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {

    let errMsg: string;
    if (error instanceof Response) {
      errMsg = `${error.status} - ${error.text() }`;
    } else {
      errMsg = error.message ? error.message : error;
    }

    return Promise.reject(errMsg);
  }

}
