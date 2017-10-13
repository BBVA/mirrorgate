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
import {ActivatedRoute, Router} from '@angular/router';
import {kebabCase} from 'lodash';

import {Dashboard} from '../../model/dashboard';
import {DashboardsService} from '../../services/dashboards.service';
import {SlackService} from '../../services/slack.service';
import {RequestOptions} from '@angular/http/http';

import {TextsService} from '../../services/texts.service';
import {ConfigService} from '../../services/config.service';

@Component({
  selector: 'new-and-edit-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
  providers: [DashboardsService, SlackService, TextsService, ConfigService]
})
export class FormComponent {
  backToDashboard: boolean;
  dashboard: Dashboard;
  slackChannels: {keys?: string[], values?: Map<string, string>} = {};
  slack: {clientId?: string, clientSecret?: string} = {};
  edit: boolean = false;
  temp: {
    applications?: string,
    boards?: string,
    codeRepos?: string,
    adminUsers?: {
      display?: string,
      value?: string
    }[],
    analyticViews?: string,
    aggregatedDashboards?: {
      display?: string,
      value?: string
    }[],
    teamMembers?: {
      display?: string,
      value?: string
    }[],
  } = {};
  errorMessage: string;
  url: string;
  icon: {error?: string, success?: boolean} = {};
  texts : {loaded?: boolean} = {loaded: false};

  constructor(
      private dashboardsService: DashboardsService,
      private textsService: TextsService,
      private slackService: SlackService,
      private router: Router,
      private route: ActivatedRoute,
      private configService: ConfigService) {}

  ngOnInit(): void {
    let id = this.route.snapshot.params['id'];
    let url = document.location.href;
    let pos = url.lastIndexOf('/backoffice/');

    this.backToDashboard = this.route.snapshot.queryParams.backToDashboard === 'true';
    if (pos > 0) {
      url = url.substring(0, pos);
    } else {
      url = '';
    }
    this.url = url;

    if (id) {
      this.edit = true;
      this.dashboardsService.getDashboard(id).then(
          dashboard => this.setDashboard(dashboard));
    } else {
      this.setDashboard(new Dashboard());
    }

    this.textsService.getTexts()
      .then((texts) => {
        this.texts = texts;
        this.texts.loaded = true;
      })
      .catch((error: any) => { this.errorMessage = <any>error; });
  }

  setDashboard(dashboard: Dashboard) {
    if (!dashboard.displayName) {
      dashboard.displayName = dashboard.name;
    }

    this.dashboard = dashboard;
    this.temp.boards =
        this.dashboard.boards ? this.dashboard.boards.join(',') : '';
    this.temp.applications = this.dashboard.applications ?
        this.dashboard.applications.join(',') :
        '';
    this.temp.codeRepos =
        this.dashboard.codeRepos ? this.dashboard.codeRepos.join(',') : '';
    this.temp.adminUsers = this.dashboard.adminUsers ?
        this.dashboard.adminUsers.map((e) => {
          return { display: e, value: e }
        }) : [];
    this.temp.aggregatedDashboards = this.dashboard.aggregatedDashboards ?
        this.dashboard.aggregatedDashboards.map((e) => {
          return { display: e, value: e }
        }) : [];
    this.temp.analyticViews = this.dashboard.analyticViews ?
        this.dashboard.analyticViews.join(',') :
        '';
    this.temp.teamMembers = this.dashboard.teamMembers ?
        this.dashboard.teamMembers.map((e) => {
          return { display: e, value: e }
        }) : [];
    this.updateSlackChannels();
  }

  mirrorTempValues() {
    this.dashboard.boards = this.temp.boards.length ?
        this.temp.boards.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.applications = this.temp.applications.length ?
        this.temp.applications.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.codeRepos = this.temp.codeRepos.length ?
        this.temp.codeRepos.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.adminUsers = this.temp.adminUsers.length ?
        this.temp.adminUsers.map((e) => e.value.split('@')[0].trim()) :
        undefined;
    this.dashboard.aggregatedDashboards = this.temp.aggregatedDashboards.length ?
        this.temp.aggregatedDashboards.map((e) => e.value.trim()) :
        undefined;
    this.dashboard.analyticViews = this.temp.analyticViews.length ?
        this.temp.analyticViews.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.teamMembers = this.temp.teamMembers.length ?
        this.temp.teamMembers.map((e) => e.value.split('@')[0].trim()) :
        undefined;
    if (!this.edit) {
      this.dashboard.name = kebabCase(this.dashboard.displayName);
    }
  }

  back(): void {
    if(this.backToDashboard) {
      this.configService.getConfig().then((config) => {
        document.location.href = config.dashboardUrl + '?board=' + encodeURIComponent(this.dashboard.name);
      });
    } else {
      this.router.navigate(['/list']);
    }
  }

  private updateSlackChannels(): void {
    this.slackService.getChannels(this.dashboard).then((channels) => {
      this.slackChannels.values = channels;
      this.slackChannels.keys = channels && Object.keys(channels);
    });
  }

  private setSlackToken(token: string): void {
    this.dashboard.slackToken = token;
    this.updateSlackChannels();
  }

  onSave(dashboard: Dashboard): void {
    this.dashboardsService.saveDashboard(dashboard, this.edit)
        .then(dashboard => {
          if (dashboard) {
            this.dashboard = dashboard;
            this.back();
          }
        })
        .catch((error: any) => { this.errorMessage = <any>error; });
  }

  signSlack(dashboard: Dashboard): void {
    this.slackService
        .signSlack(
            this.dashboard.slackTeam, this.slack.clientId,
            this.slack.clientSecret)
        .then((token) => this.setSlackToken(token))
        .catch((error: any) => { this.errorMessage = <any>error; });
  }

  uploadImage(event) {
    this.icon = {};

    let fileList: FileList = event.target.files;

    if(fileList.length > 0) {
        let file: File = fileList[0];
        this.dashboardsService.uploadImage(this.dashboard, file)
          .then(() => {
            this.icon.success = true;
            this.icon.error = undefined;
            this.dashboard.logoUrl = '#UPLOADED#';
          })
          .catch((err) => {
            this.icon.success = false;
            this.icon.error = err;
          });
    }
  }
}
