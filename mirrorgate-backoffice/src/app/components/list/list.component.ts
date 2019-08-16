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

import { Component } from '@angular/core';
import { ElementRef, Renderer, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { DashboardsService } from '../../services/dashboards.service';
import { ConfigService } from '../../services/config.service';

import { Dashboard } from '../../model/dashboard';
@Component({
  selector: 'list',
  styleUrls: ['./list.component.scss'],
  templateUrl: './list.component.html',
  providers: [DashboardsService, ConfigService]
})
export class ListComponent {
  boards: Dashboard[];
  sourceBoards: Dashboard[];
  recentBoards: Dashboard[];
  filterBoards: Dashboard[];
  maxPages: number = 0;
  itemsPerPage: number = 20;
  queryParams: {
    search: string,
    page: number
  };
  @ViewChild('searchInput', { static: true }) searchInputRef: ElementRef;

  constructor(
    private dashboardsService: DashboardsService,
    private configService: ConfigService,
    private renderer: Renderer,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.queryParams = {
      search: this.route.snapshot.queryParams.search ? this.route.snapshot.queryParams.search : '',
      page: Number(this.route.snapshot.queryParams.page ? this.route.snapshot.queryParams.page : 0)
    };
    this.router.navigate([], { queryParams: this.queryParams });
    this.getDashboards().subscribe((boards) => {
      let recentIds: string[] = JSON.parse(localStorage.getItem('recentDashboards') || '[]');
      this.sourceBoards = boards;
      this.recentBoards = boards.filter((b) => recentIds.indexOf(b.name) >= 0);
      this.filterBoards = boards.filter((b) => recentIds.indexOf(b.name) < 0);
      this.searchDashboard();
    });
    this.renderer.invokeElementMethod(
      this.searchInputRef.nativeElement,
      'focus'
    );
  }

  getDashboards(): Observable<Dashboard[]> {
    return this.dashboardsService.getDashboards()
      .pipe(map(boards => boards.sort((a, b) => {
        let nameA = (a.displayName || a.name).toUpperCase();
        let nameB = (b.displayName || b.name).toUpperCase();
        return nameA > nameB ? 1 : nameA == nameB ? 0 : -1;
    })));
  }

  openDashboard(dashboard: Dashboard) {
    document.location.href = this.configService.getConfig('MIRRORGATE_API_URL') + '?board=' + encodeURIComponent(dashboard.name);
  }

  searchDashboard(value?) {
    this.setQueryParams(value || value === '' ? value : this.queryParams.search, this.queryParams.page);
    this.filterBoards = this.sourceBoards ? this.queryParams.search && this.queryParams.search.length ?
      this.sourceBoards.filter(board => (board.displayName || board.name).toLowerCase().indexOf(this.queryParams.search.toLowerCase()) >= 0):
      this.sourceBoards : [];
    this.maxPages = Math.ceil(this.filterBoards.length/this.itemsPerPage);
    if(value === ''){
      this.setQueryParams('', 0);
      this.pagingDashboard(0);
    }else{
      this.pagingDashboard(this.queryParams.page >= 0 ? this.queryParams.page : 0);
    }
  }

  deleteSearch() {
    this.setQueryParams('', 0);
    this.searchDashboard('');
  }

  pagingDashboard(pageNumber) {
    this.boards = this.filterBoards.slice(pageNumber * this.itemsPerPage, (pageNumber + 1) * this.itemsPerPage);
    this.setQueryParams(this.queryParams.search, pageNumber);
  }

  setQueryParams(search, page) {
    this.queryParams.search = search;
    this.queryParams.page = page;
    this.router.navigate([], { queryParams: this.queryParams });
  }
}
