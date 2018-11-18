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

import {Review} from '../../model/review';
import {ReviewsService} from '../../services/reviews.service';

import {TextsService} from '../../services/texts.service';


@Component({
  selector: 'feedback-form',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.scss'],
  providers: [ReviewsService, TextsService]
})
export class FeedbackComponent {
  review: Review;
  errorMessage: string;
  texts : {loaded?: boolean} = {loaded: false};

  constructor(
    private reviewsService: ReviewsService,
    private textsService: TextsService
  ) {}

  ngOnInit(): void {
    this.review = new Review();
    this.textsService.getTexts().subscribe(
      texts => {
        this.texts = texts;
        this.texts.loaded = true;
      },
      error => this.errorMessage = error.error
    );
  }

  back(): void { window.history.back(); }

  onSave(review: Review): void {
    this.reviewsService.saveReview(review).subscribe(
      () => this.back(),
      error => {
        this.errorMessage = error;
      }
    );
  }
}
