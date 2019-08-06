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
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';


@Injectable()
export class DashboardsService {

  private dashboardsUrl = environment.mirrorGateUrl + '/dashboards';

  constructor(private http: HttpClient) { }

  getDashboards() {
    return this.http.get<Dashboard[]>(this.dashboardsUrl)
      .pipe(
        catchError(this.handleError)
      );
  }

  getDashboard(id) {
    return this.http.get<Dashboard>(this.dashboardsUrl + '/' + id + '/details')
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteDashboard(dashboard: Dashboard) {
    return this.http.delete(this.dashboardsUrl + '/' + dashboard.name)
      .pipe(
        catchError(this.handleError)
      );
  }

  saveDashboard(dashboard: Dashboard, exists?: boolean) {
    if (exists) {
      return this.http.put<Dashboard>(this.dashboardsUrl + '/' + dashboard.name, dashboard)
        .pipe(
          catchError(this.handleError)
        );
    } else {
      return this.http.post<Dashboard>(this.dashboardsUrl, dashboard)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  uploadImage(dashboard: Dashboard, file: File) {
    let formData: FormData = new FormData();
    formData.append('uploadfile', file, file.name);

    return this.http.post(`${this.dashboardsUrl}/${dashboard.name}/image`, formData, { responseType: 'text' })
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    let errMsg: string;
    if (error.error instanceof ErrorEvent) {
      errMsg = `${error.status} - ${error.error.message}`;
    } else {
      errMsg = error.error
        ? error.error.message
        : error.message ? error.message : error;
    }
    return throwError(errMsg);
  };
}
