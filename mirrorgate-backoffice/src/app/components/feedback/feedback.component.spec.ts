/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { TestBed, ComponentFixture, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { Location } from '@angular/common';

import { TextsService } from '../../services/texts.service';

import { FeedbackComponent } from './feedback.component';

import { MockReviewsService } from '../../../../test/mocks/services/mock.reviews.service';
import { ReviewsService } from '../../services/reviews.service';
import { MockTextsService } from '../../../../test/mocks/services/mock.texts.service';

describe('FeedbackComponent', () => {
  let fixture: ComponentFixture<FeedbackComponent>;
  let location: Location;

  beforeEach(async(() => {
    let mockReviewsService = new MockReviewsService();
    let mockTextsService = new MockTextsService();

    TestBed.configureTestingModule({
      declarations: [FeedbackComponent],
      imports: [HttpClientTestingModule, RouterTestingModule, FormsModule],
      providers: [ReviewsService, TextsService]
    }).compileComponents();

    TestBed.overrideComponent(FeedbackComponent, {
      set: {
        providers: [
          { provide: ReviewsService, useValue: mockReviewsService },
          { provide: TextsService, useValue: mockTextsService }
        ]
      }
    });

    fixture = TestBed.createComponent(FeedbackComponent);
    location = TestBed.get(Location);

    spyOn(location, 'back').and.stub();
  }));

  it('should allow to send feedback', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      let starsDiv = fixture.nativeElement.querySelector('#stars');
      let textArea = fixture.nativeElement.querySelector('textarea');
      let sendButton = fixture.nativeElement.querySelector('#send-button');

      textArea.value = 'This is a good comment';
      textArea.dispatchEvent(new Event('input'));
      sendButton.click();

      expect(starsDiv).toBeDefined();
      expect(location.back).toHaveBeenCalled();
    });
  }));

  it('should allow to cancel feedback', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      let cancelButton = fixture.nativeElement.querySelector('#cancel-button');

      cancelButton.click();

      expect(location.back).toHaveBeenCalled();
    });
  }));

  it('should display error if there is not comment', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      let sendButton = fixture.nativeElement.querySelector('#send-button');

      sendButton.click();
      fixture.detectChanges();
      let errorMessage = fixture.nativeElement.querySelector('.error');

      expect(errorMessage.textContent).toContain('Wrong Review Request');
      expect(location.back).not.toHaveBeenCalled();
    });
  }));
});
