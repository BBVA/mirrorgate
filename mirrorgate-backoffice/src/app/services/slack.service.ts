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

import { Injectable } from '@angular/core';

import { Dashboard } from '../model/dashboard';
import { Http, Headers, RequestOptions, URLSearchParams, Response } from '@angular/http';

import 'rxjs/add/operator/toPromise';

function getLocation(): any {
    return location;
}

@Injectable()
export class SlackService {

  constructor(private http: Http) { }

  signSlack(team:string, clientId:string, clientSecret:string): Promise<string> {
    var dummy: HTMLAnchorElement = document.createElement('a');
    dummy.href = 'utils/slack/code-capturer';

    var redirectUrl = encodeURIComponent(dummy.href);
    return new Promise((resolve, reject) =>  {
      var source = window.open(
        `https://slack.com/oauth/authorize?client_id=${clientId}&scope=client&team=${team}`
      );
      var receiver = (msg: MessageEvent) =>  {
        window.removeEventListener("message", receiver, false);
        if(msg.source == source && msg.origin == document.location.origin) {
          resolve(this.generateToken(msg.data, team, clientId, clientSecret));
        }
      };
      window.addEventListener("message", receiver, false);
    });
  }

  private generateToken(code:string, team:string, clientId:string, clientSecret:string): Promise<any> {
    let params: URLSearchParams = new URLSearchParams();
    params.set('code', code);
    params.set('team', team);
    params.set('clientId', clientId);
    params.set('clientSecret', clientSecret);

    return this.http.get('utils/slack/token-generator', {
      search: params
    }).toPromise().then((r) => {
      return r.text();
    });
  }

  getChannels(dashboard:Dashboard): Promise<Map<string,string>> {
    let params: URLSearchParams = new URLSearchParams();
    params.set('dashboard', dashboard.name);
    params.set('token', dashboard.slackToken);

    return this.http.get(`utils/slack/channels`,{
      search: params
    }).toPromise().then((r) => {
      if(r.status == 200) {
        return r.json();
      } else {
        return null;
      }
    }).catch(() => null);
  }

  private handleError(error: any): Promise<any> {

    let errMsg: string;
    if (error instanceof Response) {
      errMsg = `${error.status} - ${error.text() }`;
    } else {
      errMsg = error.message ? error.message : error;
    }

    return Promise.reject(errMsg);
  }

}
