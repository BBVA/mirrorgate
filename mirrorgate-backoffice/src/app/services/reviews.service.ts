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
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError} from 'rxjs/operators';
import { environment } from '../../environments/environment';


@Injectable()
export class ReviewsService {

  private reviewsUrl = environment.mirrorGateUrl + '/reviews/mirrorgate';

  constructor(private http: HttpClient) { }

  saveReview(review: Review) {
    let data = new HttpParams();
    data.append('rate', review.rate.toString());
    data.append('comment', review.comment);

    return this.http.post<Review>(this.reviewsUrl, data)
                .pipe(
                  catchError(this.handleError)
                );
  }

  private handleError(error: HttpErrorResponse) {
    let errMsg: string;
    if (error.error instanceof ErrorEvent) {
      errMsg = `${error.status} - ${error.error.message }`;
    } else {
      errMsg = error.error.message ? error.error.message : error;
    }
    return throwError(errMsg);
  };

}
