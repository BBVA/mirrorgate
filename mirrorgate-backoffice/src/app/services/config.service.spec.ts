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

import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';

import { TestBed } from '@angular/core/testing';

import { ConfigService } from './config.service';

describe('ConfigService', () => {
  let service: ConfigService;
  let textsUrl: string;
  let httpMock: HttpTestingController;

  const fakeConfig = {
    MIRRORGATE_API_URL: 'http://fake',
    CATEGORY: 'FAKE'
  };
  console.log();

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ConfigService]
    });

    textsUrl = 'config.json';
    service = TestBed.get(ConfigService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should get config successfully', () => {
    service.loadConfig().then(() => {
      expect(service.getConfig('MIRRORGATE_API_URL')).toBe(
        fakeConfig.MIRRORGATE_API_URL
      );
      expect(service.getConfig('CATEGORY')).toBe(fakeConfig.CATEGORY);
    });

    const req = httpMock.expectOne(textsUrl, 'Get config file');
    expect(req.request.method).toBe('GET');

    req.flush(fakeConfig);

    httpMock.verify();
  });

  it('should get an error if config does not exists', () => {
    const httpError = new HttpErrorResponse({
      error: 'Not Found Error',
      status: 404,
      statusText: 'Not Found'
    });

    service
      .loadConfig()
      .then(() => {
        expect(service.getConfig('MIRRORGATE_API_URL')).toBeUndefined();
        expect(service.getConfig('CATEGORY')).toBeUndefined();
      })
      .catch(ex => {
        expect(ex.error).toBe(httpError.error);
        expect(ex.status).toBe(httpError.status);
      });

    const req = httpMock.expectOne(textsUrl, 'Get config file');
    expect(req.request.method).toBe('GET');

    req.flush(httpError.error, httpError);

    httpMock.verify();
  });
});
