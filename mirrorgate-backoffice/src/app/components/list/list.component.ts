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
import {DashboardsService} from '../../services/dashboards.service';
import {Dashboard} from '../../model/dashboard';
import { OnInit } from '@angular/core';

@Component({
  selector: 'list',
  styleUrls: ['./list.component.scss'],
  templateUrl: './list.component.html',
  providers: [DashboardsService]
})
export class ListComponent {

  boards: Dashboard[];

  constructor(private dashboardsService: DashboardsService) {}

  ngOnInit(): void {
    this.getDashboards();
  }

  getDashboards(): Promise<Dashboard[]> {
    return this.dashboardsService.getDashboards().then(boards => this.boards = boards.sort((a, b) => {
      let nameA = (a.displayName || a.name).toUpperCase();
      let nameB = (b.displayName || b.name).toUpperCase();
      return nameA > nameB ? 1 : nameA == nameB ? 0 : -1;
    }));
  }

  getDashboardUrl(dashboard: Dashboard) {
    return '../dashboard.html?board=' + encodeURIComponent(dashboard.name);
  }
}
