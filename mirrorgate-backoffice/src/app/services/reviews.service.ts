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

import { Review } from '../model/review';
import { Http } from '@angular/http';
import { Headers, RequestOptions, Response } from '@angular/http';
import { URLSearchParams } from '@angular/http';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class ReviewsService {

  private reviewsUrl = '../api/reviews/mirrorgate';

  constructor(private http: Http) { }

  saveReview(review: Review): Promise<Review> {
    let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' });
    let options = new RequestOptions({ headers: headers });

    let data = new URLSearchParams();
    data.append('rating', review.rate.toString());
    data.append('comment', review.comment);

    return this.http.post(this.reviewsUrl, data, options)
              .toPromise()
              .then(response => response.json() as Review)
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
