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

import {Component} from '@angular/core';
import {OnInit} from '@angular/core';

import {Review} from '../../model/review';
import {ReviewsService} from '../../services/reviews.service';

@Component({
  selector: 'feedback-form',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.scss'],
  providers: [ReviewsService]
})
export class FeedbackComponent {
  review: Review;
  errorMessage: string;

  constructor(
    private reviewsService: ReviewsService
  ) {}

  ngOnInit(): void {
    this.review = new Review();
  }

  back(): void { window.history.back(); }

  onSave(review: Review): void {
    this.reviewsService.saveReview(review)
        .then(() => {
          this.back();
        })
        .catch((error: any) => { this.errorMessage = <any>error; });
  }
}
