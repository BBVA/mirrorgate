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

import {
  TestBed,
  ComponentFixture,
  async,
  inject
} from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { Component } from '@angular/core';
import { Location } from '@angular/common';

import { Router, ActivatedRoute } from '@angular/router';

import { Dashboard } from '../../model/dashboard';

import { DashboardsService } from '../../services/dashboards.service';
import { ConfigService } from '../../services/config.service';
import { TextsService } from '../../services/texts.service';

import { MockDashboardsService } from '../../../../test/mocks/services/mock.dashboards.service';

import { ListComponent } from './list.component';

import { MockConfigService } from '../../../../test/mocks/services/mock.config.service';


describe('ListComponent', () => {
  let fixture: ComponentFixture<ListComponent>;
  let router: Router;

  let fakeDashboard: Dashboard;
  let fakeDashboard2: Dashboard;
  let fakeDashboards: Dashboard[];
  let mockDashboardsService: MockDashboardsService;
  let routeStub = {
    snapshot: { queryParams: new Map() }
  };

  @Component({ template: '' })
  class DummyRouterComponent {}

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

    TestBed.configureTestingModule({
      declarations: [ListComponent, DummyRouterComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([
          { path: 'delete/:dashboard', component: DummyRouterComponent },
          { path: 'edit/:dashboard', component: DummyRouterComponent }
        ]),
        FormsModule
      ],
      providers: [DashboardsService, ConfigService, TextsService]
    }).compileComponents();

    TestBed.overrideComponent(ListComponent, {
      set: {
        providers: [
          { provide: ActivatedRoute, useValue: routeStub },
          { provide: DashboardsService, useValue: mockDashboardsService },
          { provide: ConfigService, useClass: MockConfigService }
        ]
      }
    });

    router = TestBed.get(Router);
    spyOn(router, 'navigate').and.stub();

    fixture = TestBed.createComponent(ListComponent);
  }));

  it('should display a search input and a list with all dashboards', async(() => {
    routeStub.snapshot.queryParams['search'] = '';
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      let searchInput = fixture.nativeElement.querySelector('#search-input');
      let items = fixture.nativeElement.querySelectorAll('.boards-list-item');

      expect(searchInput).toBeDefined();
      expect(items.length).toBe(fakeDashboards.length);
    });
  }));

  it('should filter dashboards list when search param has a value', async(() => {
    routeStub.snapshot.queryParams['search'] = fakeDashboard.name;
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      let items = fixture.nativeElement.querySelectorAll('.boards-list-item');
      expect(items.length).toBe(1);
    });
  }));

  it('should filter dashboards list when search input changes value', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      let searchInput = fixture.nativeElement.querySelector('#search-input');
      searchInput.value = fakeDashboard.name;
      searchInput.dispatchEvent(new Event('input'));

      fixture.detectChanges();

      let items = fixture.nativeElement.querySelectorAll('.boards-list-item');

      expect(items.length).toBe(1);
      let queryParams = {
        search: fakeDashboard.name,
        page: 0
      };

      expect(router.navigate).toHaveBeenCalledWith([], {
        queryParams: queryParams
      });
    });
  }));

  it('should allow to edit a dashboard', async(
    inject([Location], (location: Location) => {
      fixture.detectChanges();
      let items = fixture.nativeElement.querySelectorAll('.boards-list-item');
      let editButton = items[0]
        .querySelector('.fa-pencil-alt')
        .closest('button');

      editButton.click();

      fixture.whenStable().then(() => {
        expect(location.path()).toEqual('/edit/' + fakeDashboards[0].name);
      });
    })
  ));

  it('should allow to delete a dashboard', async(
    inject([Location], (location: Location) => {
      fixture.detectChanges();
      let items = fixture.nativeElement.querySelectorAll('.boards-list-item');
      let deleteButton = items[0]
        .querySelector('.fa-trash-alt')
        .closest('button');

      deleteButton.click();

      fixture.whenStable().then(() => {
        expect(location.path()).toEqual('/delete/' + fakeDashboards[0].name);
      });
    })
  ));
});
