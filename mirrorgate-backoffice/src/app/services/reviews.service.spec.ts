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

import { ReviewsService } from './reviews.service';
import { ConfigService } from './config.service';

import { Review } from '../model/review';

import { MockConfigService } from '../../../test/mocks/services/mock.config.service';

describe('ReviewsService', () => {
  let service: ReviewsService;
  let reviewsUrl: string;
  let httpMock: HttpTestingController;

  const fakeReview = new Review();
  fakeReview.comment = 'Awesome!!';
  fakeReview.rate = 5.0;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ReviewsService,
        { provide: ConfigService, useClass: MockConfigService }
      ]
    });

    reviewsUrl =
      TestBed.get(ConfigService).getConfig('MIRRORGATE_API_URL') +
      '/reviews/mirrorgate';
    service = TestBed.get(ReviewsService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should save a review successfully', () => {
    service.saveReview(fakeReview).subscribe((data: any) => {
      expect(data.comment).toBe(fakeReview.comment);
      expect(data.rate).toBe(fakeReview.rate);
    });

    const req = httpMock.expectOne(
      reviewsUrl,
      'Save review with comment: ' + fakeReview.comment
    );
    expect(req.request.method).toBe('POST');

    req.flush(fakeReview);

    httpMock.verify();
  });
});
