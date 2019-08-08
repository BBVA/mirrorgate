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

import { Router, ActivatedRoute } from '@angular/router';

import { Dashboard } from '../../model/dashboard';

import { DashboardsService } from '../../services/dashboards.service';
import { TextsService } from '../../services/texts.service';

import { MockDashboardsService } from '../../../../test/mocks/services/mock.dashboards.service';

import { DeleteComponent } from './delete.component';

describe('DeleteComponent', () => {
  let fixture: ComponentFixture<DeleteComponent>;
  let router: Router;

  let fakeDashboard: Dashboard;
  let mockDashboardsService: MockDashboardsService;
  let routeStub = {
    snapshot: { params: new Map() }
  };

  beforeEach(async(() => {

    fakeDashboard = new Dashboard();
    fakeDashboard.name = 'mirrorgate';
    fakeDashboard.displayName = 'MirrorGate';

    mockDashboardsService = new MockDashboardsService();
    mockDashboardsService.setFakeDashboard(fakeDashboard);

    TestBed.configureTestingModule({
      declarations: [DeleteComponent],
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [DashboardsService, TextsService]
    }).compileComponents();

    TestBed.overrideComponent(DeleteComponent, {
      set: {
        providers: [
          { provide: ActivatedRoute, useValue: routeStub },
          { provide: DashboardsService, useValue: mockDashboardsService }
        ]
      }
    });

    router = TestBed.get(Router);
    spyOn(router, 'navigate').and.stub();

    fixture = TestBed.createComponent(DeleteComponent);

  }));

  it('should delete dashboard when #delete-button is click', async(() => {
    routeStub.snapshot.params['id'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let header = fixture.nativeElement.querySelector('h1');
      let deleteButton: HTMLElement = fixture.nativeElement.querySelector('#delete-button');
      deleteButton.click();

      expect(header.textContent).toContain(fakeDashboard.name);
      expect(router.navigate).toHaveBeenCalledWith(['/list']);
    });
  }));

  it('should show error if dashboard does not exist', async(() => {
    routeStub.snapshot.params['id'] = undefined;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let errorMessage: HTMLElement = fixture.nativeElement.querySelector('.error');

      expect(errorMessage.textContent).toContain('Dashboard Not Found');
    });
  }));

  it('should show error if dashboard is not deleted', async(() => {
    routeStub.snapshot.params['id'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      // Force delete to fail
      mockDashboardsService.setFakeDashboard(undefined);

      let deleteButton: HTMLElement = fixture.nativeElement.querySelector('#delete-button');
      deleteButton.click();
      fixture.detectChanges();

      let errorMessage: HTMLElement = fixture.nativeElement.querySelector('.error');

      expect(errorMessage.textContent).toContain('Dashboard Not Found');
    });
  }));

  it('should cancel deletion when #cancel-button is click', async(() => {
    routeStub.snapshot.params['id'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let cancelButton: HTMLElement = fixture.nativeElement.querySelector('#cancel-button');
      cancelButton.click();

      expect(router.navigate).toHaveBeenCalledWith(['/list']);
    });
  }));
});
