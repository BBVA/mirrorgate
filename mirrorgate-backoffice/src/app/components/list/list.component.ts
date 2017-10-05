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
import {ElementRef, OnInit, Renderer, ViewChild} from '@angular/core';

import {ConfigService} from '../../services/config.service';

@Component({
  selector: 'list',
  styleUrls: ['./list.component.scss'],
  templateUrl: './list.component.html',
  providers: [DashboardsService, ConfigService]
})
export class ListComponent {
  boards: Dashboard[];
  sourceBoards: Dashboard[];
  searchText: string = '';
  filterBoards: Dashboard[];
  pageNumber: number = 0;
  maxPages: number = 0;
  itemsPerPage: number = 20;
  @ViewChild('searchInput') searchInputRef: ElementRef;

  constructor(
    private dashboardsService: DashboardsService,
    private configService: ConfigService,
    private renderer: Renderer
  ) {}

  ngOnInit(): void {
    this.getDashboards().then((boards) => {
      this.sourceBoards = this.filterBoards = boards;
      this.searchDashboard();
    });
    this.renderer.invokeElementMethod(
      this.searchInputRef.nativeElement,
      'focus'
    );
  }

  getDashboards(): Promise<Dashboard[]> {
    return this.dashboardsService.getDashboards().then(boards => boards.sort((a, b) => {
      let nameA = (a.displayName || a.name).toUpperCase();
      let nameB = (b.displayName || b.name).toUpperCase();
      return nameA > nameB ? 1 : nameA == nameB ? 0 : -1;
    }));
  }

  openDashboard(dashboard: Dashboard) {
    this.configService.getConfig().then((config) => {
      document.location.href = config.dashboardUrl + '?board=' + encodeURIComponent(dashboard.name);
    });
  }

  searchDashboard(value?) {
    this.searchText = value;
    this.filterBoards = value && value.length ?
      this.sourceBoards.filter(board => (board.displayName || board.name).toLowerCase().indexOf(value.toLowerCase()) >= 0):
      this.sourceBoards;
    this.maxPages = Math.ceil(this.filterBoards.length/this.itemsPerPage);
    this.pagingDashboard(0);
  }

  pagingDashboard(pageNumber) {
    this.boards = this.filterBoards.slice(pageNumber * this.itemsPerPage, (pageNumber + 1) * this.itemsPerPage);
    this.pageNumber = pageNumber;
  }
}
