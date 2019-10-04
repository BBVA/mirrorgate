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

import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { TestBed } from '@angular/core/testing';

import { SlackService } from './slack.service';
import { ConfigService } from './config.service';

import { Dashboard } from '../model/dashboard';

import { MockConfigService } from '../../../test/mocks/services/mock.config.service';

describe('SlackService', () => {
  let service: SlackService;
  let httpMock: HttpTestingController;
  let mirrorGateAPI: string;
  let slackTokenGeneratorUrl: string;
  let slackGetChannelsUrl: string;

  const fakeTeam = 'MirrorGate';
  const fakeClientId = '1111111';
  const fakeClientSecret = 'AAAAAAA';
  const fakeSlackToken = 'BBBBBBB';
  const fakeSlackChannels = {"1":"general","2":"mirrorgate","3":"alerts"};
  const slackUrl = `https://slack.com/oauth/authorize?client_id=${fakeClientId}&scope=client&team=${fakeTeam}`;

  const fakeDashboard = new Dashboard();
  fakeDashboard.name = 'mirrorgate';
  fakeDashboard.slackTeam = fakeTeam;
  fakeDashboard.slackToken = fakeSlackToken;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        SlackService,
        { provide: ConfigService, useClass: MockConfigService }
      ]
    });

    service = TestBed.get(SlackService);
    httpMock = TestBed.get(HttpTestingController);
    mirrorGateAPI = TestBed.get(ConfigService).getConfig('MIRRORGATE_API_URL');
    slackTokenGeneratorUrl = `${mirrorGateAPI}/slack/token-generator?code=${fakeSlackToken}&team=${fakeTeam}&clientId=${fakeClientId}&clientSecret=${fakeClientSecret}`;
    slackGetChannelsUrl  = `${mirrorGateAPI}/slack/channels?dashboard=${fakeDashboard.name}&token=${fakeDashboard.slackToken}`;

    spyOn(window, 'open').and.stub();
  });

  it('should sign slack successfully', () => {
    service.signSlack(fakeTeam, fakeClientId, fakeClientSecret).then((data: any) => {
      expect(data).toBe(fakeSlackToken);
    });

    let messageEvent = new MessageEvent('message', {
      data: fakeSlackToken,
      origin: document.location.origin
    });

    window.dispatchEvent(messageEvent);

    expect(window.open).toHaveBeenCalledWith(slackUrl);
    let req = httpMock.expectOne(slackTokenGeneratorUrl, 'Get Slack Token');
    expect(req.request.method).toBe('GET');

    req.flush(fakeSlackToken);

    httpMock.verify();
  });

  it('should get channels successfully', () => {
    service.getChannels(fakeDashboard).subscribe((data) => {
      expect(data).toBe(fakeSlackChannels);
    });
    let req = httpMock.expectOne(slackGetChannelsUrl, 'Get Slack Channels');
    expect(req.request.method).toBe('GET');

    req.flush(fakeSlackChannels);

    httpMock.verify();
  });
});
