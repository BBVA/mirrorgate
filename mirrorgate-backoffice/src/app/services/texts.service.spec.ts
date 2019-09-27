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

import { TestBed } from '@angular/core/testing';

import { TextsService } from './texts.service';

describe('TextsService', () => {
  let service: TextsService;
  let textsUrl: string;
  let httpMock: HttpTestingController;

  const fakeTexts = {
    title: 'mirrorgate',
    author: 'Atreyu'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TextsService]
    });

    textsUrl = 'texts.json';
    service = TestBed.get(TextsService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should get texts successfully', () => {
    service.getTexts().subscribe((data: any) => {
      expect(data.title).toBe(fakeTexts.title);
      expect(data.author).toBe(fakeTexts.author);
    });

    const req = httpMock.expectOne(textsUrl, 'Get texts');
    expect(req.request.method).toBe('GET');

    req.flush(fakeTexts);

    httpMock.verify();
  });
});
