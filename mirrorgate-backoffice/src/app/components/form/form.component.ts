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
import {OnInit, AfterViewChecked} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {kebabCase} from 'lodash';

import {Dashboard} from '../../model/dashboard';
import {DashboardsService} from '../../services/dashboards.service';
import {SlackService} from '../../services/slack.service';
import {RequestOptions} from '@angular/http/http';

import {TextsService} from '../../services/texts.service';
import {ConfigService} from '../../services/config.service';
import {DragulaService} from 'ng2-dragula';

@Component({
  selector: 'new-and-edit-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
  providers: [DashboardsService, SlackService, TextsService, ConfigService, DragulaService]
})
export class FormComponent {
  backToDashboard: boolean;
  dashboard: Dashboard;
  slackChannels: {keys?: string[], values?: Map<string, string>} = {};
  slack: {clientId?: string, clientSecret?: string} = {};
  edit: boolean = false;
  columnsUpdated: boolean = false;
  temp: {
    displayName?: string,
    logoUrl?: string,
    applications?: string,
    boards?: string,
    programIncrement?: string,
    codeRepos?: string,
    adminUsers?: {
      display?: string,
      value?: string
    }[],
    gitRepos?: string,
    analyticViews?: string,
    operationViews?: string,
    infraCost?: boolean,
    aggregatedDashboards?: {
      display?: string,
      value?: string
    }[],
    teamMembers?: {
      display?: string,
      value?: string
    }[],
    lastVersion?: string,
    slackTeam?: string,
    urlAlerts?: string,
    urlAlertsAuthorization?: string
  } = {};
  errorMessage: string;
  url: string;
  icon: {error?: string, success?: boolean} = {};
  texts : {loaded?: boolean} = {loaded: false};
  categories?: {
    display?: string,
    value?: string
  }[];
  marketsStatsDays: number;
  dashboardList: string[] = [];

  readonly MAX_COLUMNS = 5;

  constructor(
      private dashboardsService: DashboardsService,
      private textsService: TextsService,
      private slackService: SlackService,
      private router: Router,
      private route: ActivatedRoute,
      private configService: ConfigService,
      private dragulaService: DragulaService) {}

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

    this.configService.getConfig().then((config) => {
      this.categories = config.categories;
    });

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

    this.dashboardsService.getDashboards().then(dashboards => {
      this.dashboardList = dashboards.map(dashboard => dashboard.name);
    });

    this.dragulaService.setOptions('columns', {

      revertOnSpill: true,

      accepts: function(el, target, source) {

        if(target.classList.contains('dashboard-cols-template')){
          var elements = target.getElementsByClassName('dashboard-cols-module');
          var total_size = 0;

          for(var i = 0; i < elements.length; i++){
            total_size += Number(elements[i].getAttribute('size'));
            if(total_size > 4){
              return false;
            }
          }
        }

        return true;
      },

    });

  }

  setDashboard(dashboard: Dashboard) {
    if (!dashboard.displayName) {
      dashboard.displayName = dashboard.name;
    }

    this.dashboard = dashboard;
    this.temp.displayName =
        this.dashboard.displayName ? this.dashboard.displayName : '';
    this.temp.logoUrl =
        this.dashboard.logoUrl ? this.dashboard.logoUrl : '';
    this.temp.boards =
        this.dashboard.boards ? this.dashboard.boards.join(',') : '';
    this.temp.programIncrement =
        this.dashboard.programIncrement ? this.dashboard.programIncrement : '';
    this.temp.applications = this.dashboard.applications ?
        this.dashboard.applications.join(',') :
        '';
    this.temp.codeRepos =
        this.dashboard.codeRepos ? this.dashboard.codeRepos.join(',') : '';
    this.temp.adminUsers = this.dashboard.adminUsers ?
        this.dashboard.adminUsers.map((e) => {
          return { display: e, value: e }
        }) : [];
    this.temp.gitRepos =
        this.dashboard.gitRepos ? this.dashboard.gitRepos.join(',') : '';
    this.temp.aggregatedDashboards = this.dashboard.aggregatedDashboards ?
        this.dashboard.aggregatedDashboards.map((e) => {
          return { display: e, value: e }
        }) : [];
    this.temp.analyticViews = this.dashboard.analyticViews ?
        this.dashboard.analyticViews.join(',') :
        '';
    this.temp.operationViews = this.dashboard.operationViews ?
            this.dashboard.operationViews.join(',') :
            '';
    this.temp.infraCost = this.dashboard.infraCost || false;
    this.temp.teamMembers = this.dashboard.teamMembers ?
        this.dashboard.teamMembers.map((e) => {
          return { display: e, value: e }
        }) : [];
    this.temp.lastVersion =
        this.dashboard.lastVersion ? this.dashboard.lastVersion : '';
    this.temp.slackTeam =
        this.dashboard.slackTeam ? this.dashboard.slackTeam : '';
    this.temp.urlAlerts =
        this.dashboard.urlAlerts ? this.dashboard.urlAlerts : '';
    this.temp.urlAlertsAuthorization =
        this.dashboard.urlAlertsAuthorization ? this.dashboard.urlAlertsAuthorization : '';
    this.updateSlackChannels();
  }

  mirrorTempValues() {
    this.dashboard.displayName = this.temp.displayName.length ?
        this.temp.displayName.trim() : undefined;
    this.dashboard.logoUrl = this.temp.logoUrl.length ?
        this.temp.logoUrl.trim() : undefined;
    this.dashboard.boards = this.temp.boards.length ?
        this.temp.boards.split(',') : undefined;
    this.dashboard.programIncrement = this.temp.programIncrement.length ?
        this.temp.programIncrement : undefined;
    this.dashboard.applications = this.temp.applications.length ?
        this.temp.applications.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.codeRepos = this.temp.codeRepos.length ?
        this.temp.codeRepos.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.adminUsers = this.temp.adminUsers.length ?
        this.temp.adminUsers.map((e) => e.value.split('@')[0].trim()) :
        undefined;
    this.dashboard.gitRepos = this.temp.gitRepos.length ?
        this.temp.gitRepos.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.aggregatedDashboards = this.temp.aggregatedDashboards.length ?
        this.temp.aggregatedDashboards.map((e) => e.value.trim()) :
        undefined;
    this.dashboard.analyticViews = this.temp.analyticViews.length ?
        this.temp.analyticViews.split(',').map((e) => e.trim()) :
        undefined;
    this.dashboard.operationViews = this.temp.operationViews.length ?
            this.temp.operationViews.split(',').map((e) => e.trim()) :
            undefined;
    this.dashboard.infraCost = this.temp.infraCost || false;
    this.dashboard.teamMembers = this.temp.teamMembers.length ?
        this.temp.teamMembers.map((e) => e.value.split('@')[0].trim()) :
        undefined;
    this.dashboard.lastVersion = this.temp.lastVersion.length ?
        this.temp.lastVersion.trim() : undefined;
    this.dashboard.slackTeam = this.temp.slackTeam.length ?
        this.temp.slackTeam.trim() : undefined;
    this.dashboard.urlAlerts = this.temp.urlAlerts.length ?
        this.temp.urlAlerts.trim() : undefined;
    this.dashboard.urlAlertsAuthorization = this.temp.urlAlertsAuthorization.length ?
        this.temp.urlAlertsAuthorization.trim() : undefined;
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

  private updateColumns() {
    if (this.columnsUpdated || !this.dashboard || !this.dashboard.columns || !document.getElementById('columns')) {
      return;
    }

    this.columnsUpdated = true;

    this.dashboard.columns.forEach((column, index) => {
      let column_element = document.getElementById(`col${index+1}`);
      column.forEach(id => {
        let module_element  = document.getElementById(id)
        column_element.appendChild(module_element);
      });
    });

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
    document.getElementById('dynamicDashboardConfiguration') && this.saveColumns(dashboard);
    this.dashboardsService.saveDashboard(dashboard, this.edit)
        .then(dashboard => {
          if (dashboard) {
            this.dashboard = dashboard;
            this.back();
          }
        })
        .catch((error: any) => { this.errorMessage = <any>error; });
  }

  private saveColumns(dashboard: Dashboard) {
    dashboard.columns = [];

    for(let i = 0; i < this.MAX_COLUMNS; i++){
      this.dashboard.columns[i] = []
      Array.from(document.getElementById(`col${i+1}`).children).forEach(el => {
        this.dashboard.columns[i][this.dashboard.columns[i].length] = el.id;
      });
    };
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

  ngAfterViewChecked() {
    this.updateColumns();
  }

}
