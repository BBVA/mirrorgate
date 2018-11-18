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
import { HttpClient, HttpParams } from '@angular/common/http';


function getLocation(): any {
    return location;
}

@Injectable()
export class SlackService {

  constructor(private http: HttpClient) { }

  signSlack(team:string, clientId:string, clientSecret:string): Promise<string> {
    var dummy: HTMLAnchorElement = document.createElement('a');
    dummy.href = 'utils/slack/code-capturer';

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

  private generateToken(code:string, team:string, clientId:string, clientSecret:string): Promise<any>{
    let params: HttpParams = new HttpParams();
    params.set('code', code);
    params.set('team', team);
    params.set('clientId', clientId);
    params.set('clientSecret', clientSecret);

    return this.http.get('utils/slack/token-generator', {
      params: params
    }).toPromise().then((r) => {
      return r['text'];
    });
  }

  getChannels(dashboard:Dashboard): Promise<Map<string,string>> {
    let params: HttpParams = new HttpParams();
    params.set('dashboard', dashboard.name);
    params.set('token', dashboard.slackToken);

    return this.http.get(`utils/slack/channels`,{
      params: params
    }).toPromise().then((r) => {
      if(r['status'] == 200) {
        return r;
      } else {
        return null;
      }
    }).catch(() => null);
  }

}
