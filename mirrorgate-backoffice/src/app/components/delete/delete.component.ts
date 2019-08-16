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
import { Router, ActivatedRoute } from '@angular/router';

import { DashboardsService } from '../../services/dashboards.service';
import { TextsService } from '../../services/texts.service';

import { Dashboard } from '../../model/dashboard';

@Component({
  selector: 'delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss'],
  providers: [DashboardsService, TextsService]
})
export class DeleteComponent {
  dashboard: Dashboard;
  errorMessage: string;
  texts: { loaded?: boolean } = { loaded: false };

  constructor(
    private dashboardsService: DashboardsService,
    private textsService: TextsService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    let name = this.route.snapshot.params['id'];
    this.dashboardsService
      .getDashboard(name)
      .subscribe(
        dashboard => this.dashboard = dashboard,
        error => this.errorMessage = error.message || error.error && error.error.message || error.error || error
      );
    this.textsService.getTexts().subscribe(
      texts => {
        this.texts = texts;
        this.texts.loaded = true;
      },
      error => this.errorMessage = error.message || error.error && error.error.message || error.error || error
    );
  }

  back(): void {
    this.router.navigate(['/list']);
  }

  delete(): void {
    this.dashboardsService.deleteDashboard(this.dashboard).subscribe(
      () => this.back(),
      error => this.errorMessage = error.message || error.error && error.error.message || error.error || error
    );
  }
}
