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
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Router, ActivatedRoute } from '@angular/router';

import { Dashboard } from '../../model/dashboard';

import { DashboardsService } from '../../services/dashboards.service';
import { TextsService } from '../../services/texts.service';
import { ConfigService } from '../../services/config.service';

import { MockDashboardsService } from '../../../../test/mocks/services/mock.dashboards.service';
import { MockTextsService } from '../../../../test/mocks/services/mock.texts.service';
import { MockConfigService } from '../../../../test/mocks/services/mock.config.service';
import { SlackService } from '../../services/slack.service';

import { FormComponent } from './form.component';
import { MatChipsModule, MatAutocompleteModule } from '@angular/material';
import { DragulaModule } from 'ng2-dragula';

describe('FormComponent', () => {
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;

  let fakeDashboard: Dashboard;
  let fakeDashboard2: Dashboard;
  let fakeDashboards: Dashboard[];
  let mockDashboardsService: MockDashboardsService;

  let routeStub = {
    snapshot: {
      params: new Map(),
      queryParams: new Map()
    }
  };

  beforeEach(async(() => {
    fakeDashboard = new Dashboard();
    fakeDashboard.name = 'mirrorgate';
    fakeDashboard.displayName = 'MirrorGate';
    fakeDashboard2 = new Dashboard();
    fakeDashboard2.name = 'multi-product';
    fakeDashboards = [fakeDashboard, fakeDashboard2];

    mockDashboardsService = new MockDashboardsService();
    mockDashboardsService.setFakeDashboard(fakeDashboard);
    mockDashboardsService.setFakeDashboards(fakeDashboards);

    let mockTextsService = new MockTextsService();

    TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        FormsModule,
        ReactiveFormsModule,
        MatChipsModule,
        MatAutocompleteModule,
        DragulaModule.forRoot()
      ],
      providers: [DashboardsService, TextsService, ConfigService, SlackService]
    }).compileComponents();

    TestBed.overrideComponent(FormComponent, {
      set: {
        providers: [
          { provide: ActivatedRoute, useValue: routeStub },
          { provide: DashboardsService, useValue: mockDashboardsService },
          { provide: TextsService, useValue: mockTextsService },
          { provide: ConfigService, useClass: MockConfigService }
        ]
      }
    });

    fixture = TestBed.createComponent(FormComponent);
    router = TestBed.get(Router);

    spyOn(router, 'navigate').and.stub();
  }));

  it('should allow to save a dashboard', async(() => {
    routeStub.snapshot.params['id'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let displayName = fixture.nativeElement.querySelector('#displayName');
      let saveButton = fixture.nativeElement.querySelector('#save-button');
      let component = fixture.componentInstance;

      displayName.value = 'New Name';
      displayName.dispatchEvent(new Event('input'));

      saveButton.click();

      expect(component.dashboard.displayName).toBe('New Name')
      expect(router.navigate).toHaveBeenCalledWith(['/list']);
    });
  }));

  it('should show error if dashboard does not exist', async(() => {
    routeStub.snapshot.params['id'] = 'DoesNotExist';
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let errorMessage = fixture.nativeElement.querySelector('.error');

      expect(errorMessage.textContent).toContain('Dashboard Not Found');
    });
  }));

  it('should show error if dashboard is not saved', async(() => {
    routeStub.snapshot.params['id'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      // Force delete to fail
      mockDashboardsService.setFakeDashboard(undefined);

      let saveButton = fixture.nativeElement.querySelector('#save-button');

      saveButton.click();
      fixture.detectChanges();

      let errorMessage: HTMLElement = fixture.nativeElement.querySelector('.error');

      expect(errorMessage.textContent).toContain('Dashboard Not Found');
    });
  }));

  it('should allow to cancel edition', async(() => {
    routeStub.snapshot.params['id'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let cancelButton: HTMLElement = fixture.nativeElement.querySelector('#cancel-button');

      cancelButton.click();

      expect(router.navigate).toHaveBeenCalledWith(['/list']);
    });
  }));

  it('should allow to reset fields', async(() => {
    routeStub.snapshot.params['id'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let displayName = fixture.nativeElement.querySelector('#displayName');
      let resetButton = fixture.nativeElement.querySelector('#reset-button');

      displayName.value = 'New Name';
      displayName.dispatchEvent(new Event('input'));

      resetButton.click();

      expect(displayName.value).toEqual('');
    });
  }));

});