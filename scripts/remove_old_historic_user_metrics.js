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

var db;

if (typeof mongo_user == 'undefined') {
    var conn = new Mongo();
    db = conn.getDB(mongo_authdb);
} else {
    db = connect(mongo_host + ':' + mongo_port + '/' + mongo_authdb);
    db.auth(mongo_user, mongo_pass);
    db = db.getSiblingDB(mongo_authdb);
}

var TWO_MONTHS_AGO_DATE = new Date(
    new Date().setMonth(new Date().getMonth() - 2)
);
var TWO_DAYS_AGO_DATE = new Date(new Date().setDate(new Date().getDate() - 2));
var TWO_HOURS_AGO_DATE = new Date(
    new Date().setHours(new Date().getHours() - 2)
);

'Removing old historic user metrics of type DAYS until: ' + TWO_MONTHS_AGO_DATE;
db.getCollection('historic_user_metrics').remove({
    timestamp: { $lt: TWO_MONTHS_AGO_DATE.getTime() },
    historicType: 'DAYS'
});

'Removing old historic user metrics of type HOURS until: ' + TWO_DAYS_AGO_DATE;
db.getCollection('historic_user_metrics').remove({
    timestamp: { $lt: TWO_DAYS_AGO_DATE.getTime() },
    historicType: 'HOURS'
});

'Removing old historic user metrics of type MINUTES until: ' +
    TWO_HOURS_AGO_DATE;
db.getCollection('historic_user_metrics').remove({
    timestamp: { $lt: TWO_HOURS_AGO_DATE.getTime() },
    historicType: 'MINUTES'
});
