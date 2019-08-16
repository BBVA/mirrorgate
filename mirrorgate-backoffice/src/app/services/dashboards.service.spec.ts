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

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { DashboardsService } from './dashboards.service';
import { ConfigService } from './config.service';

import { Dashboard } from '../model/dashboard';

import { MockConfigService } from '../../../test/mocks/services/mock.config.service';

describe('DashboardsService', () => {
  let service: DashboardsService;
  let dashboardsUrl: string;
  let httpMock: HttpTestingController;

  const fakeDashboard = new Dashboard();
  fakeDashboard.name = 'mirrorgate';
  fakeDashboard.displayName = 'MirrorGate';

  const fakeDashboards = [
    fakeDashboard,
    {
      name: 'multiProduct-dashboard'
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        DashboardsService,
        { provide: ConfigService, useClass: MockConfigService }
      ]
    });

    dashboardsUrl = TestBed.get(ConfigService).getConfig('MIRRORGATE_API_URL') + '/dashboards';
    service = TestBed.get(DashboardsService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should get dashboards successfully', () => {
    service.getDashboards().subscribe((data: any) => {
      expect(data.length).toBe(2);
    });

    const req = httpMock.expectOne(dashboardsUrl, 'Get all dashboards');
    expect(req.request.method).toBe('GET');

    req.flush(fakeDashboards);

    httpMock.verify();
  });

  it('should get a dashboard successfully', () => {
    service.getDashboard(fakeDashboard.name).subscribe((data: any) => {
      expect(data.name).toBe(fakeDashboard.name);
    });

    const req = httpMock.expectOne(dashboardsUrl + '/' + fakeDashboard.name + '/details', 'Get details of ' + fakeDashboard.name);
    expect(req.request.method).toBe('GET');

    req.flush(fakeDashboard);

    httpMock.verify();
  });

  it('should delete a dashboard successfully', () => {
    service.deleteDashboard(fakeDashboard).subscribe((data: any) => {
      expect(JSON.parse(data).name).toEqual(fakeDashboard.name);
    });

    const req = httpMock.expectOne(dashboardsUrl + '/' + fakeDashboard.name, 'Delete dashboard ' + fakeDashboard.name);
    expect(req.request.method).toBe('DELETE');

    req.flush(fakeDashboard);

    httpMock.verify();
  });

  it('should save a dashboard successfully', () => {
    service.saveDashboard(fakeDashboard).subscribe((data: any) => {
      expect(data.name).toBe(fakeDashboard.name);
    });

    const req = httpMock.expectOne(dashboardsUrl, 'Save dashboard ' + fakeDashboard.name);
    expect(req.request.method).toBe('POST');

    req.flush(fakeDashboard);

    httpMock.verify();
  });

  it('should update a dashboard successfully', () => {
    service.saveDashboard(fakeDashboard, true).subscribe((data: any) => {
      expect(data.name).toBe(fakeDashboard.name);
    });

    const req = httpMock.expectOne(dashboardsUrl + '/' + fakeDashboard.name, 'Update dashboard ' + fakeDashboard.name);
    expect(req.request.method).toBe('PUT');

    req.flush(fakeDashboard);

    httpMock.verify();
  });

  it('should upload an image of a dashboard successfully', () => {
    const fakeFile = new File([], 'test-file.jpg', { type: 'image/jpeg' });
    const fakeMessage = 'Upload Image';

    service.uploadImage(fakeDashboard, fakeFile).subscribe((data: any) => {
      expect(data).toBe(fakeMessage);
    });

    const req = httpMock.expectOne(dashboardsUrl + '/' + fakeDashboard.name + '/image', 'Upload an image to dashboard ' + fakeDashboard.name);
    expect(req.request.method).toBe('POST');

    req.flush(fakeMessage);

    httpMock.verify();
  });

  it('should get an error if dashboard does not exists', () => {
    const fakeName = 'fakeName';
    service.getDashboard(fakeName).subscribe(
      (error: any) => { expect(error.status).toBe(404);}
    );

    const req = httpMock.expectOne(dashboardsUrl + '/'+ fakeName +'/details', 'Get details of fakeName');
    expect(req.request.method).toBe('GET');

    req.flush('Not Found Error', new HttpErrorResponse({
      error: 'test 404 error',
      status: 404,
      statusText: 'Not Found'
    }));

    httpMock.verify();
  });
});
