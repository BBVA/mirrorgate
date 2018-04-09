import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { DeleteComponent } from '../delete/delete.component';
import { DragulaModule, DragulaService } from 'ng2-dragula';
import { FeedbackComponent } from '../feedback/feedback.component';
import { FormComponent } from '../form/form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { HttpModule } from '@angular/http';
import { ListComponent } from '../list/list.component';
import { NgModule } from '@angular/core';
import { rootRouterConfig } from './app.routes';
import { RouterModule } from '@angular/router';
import { TagInputModule } from 'ngx-chips';
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





@NgModule({
  declarations: [
    AppComponent,
    ListComponent,
    FormComponent,
    FeedbackComponent,
    DeleteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    RouterModule.forRoot(rootRouterConfig, { useHash: true }),
    TagInputModule,
    BrowserAnimationsModule,
    DragulaModule
  ],
  providers: [
    DragulaService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {

  constructor(private dragulaService: DragulaService) {
    dragulaService.setOptions('columns', {

      copySortSource: false,
      revertOnSpill: false,
      removeOnSpill: true,

      accepts: function(el, target, source) {

        var componentWeight = {
        };

        componentWeight['current-sprint'] = 2;
        componentWeight['program-increment'] = 2;
        componentWeight['bugs'] = 1;
        componentWeight['scm-metrics'] = 1;
        componentWeight['alerts'] = 2;
        componentWeight['next-sprint'] = 1;
        componentWeight['builds'] = 2;
        componentWeight['buildsstats'] = 1;
        componentWeight['markets'] = 1;
        componentWeight['reviews'] = 1;
        componentWeight['user-metrics'] = 1;
        componentWeight['operations-metrics'] = 1;

        if(target.classList.contains('dashboard-cols-template')){
          var elements = target.getElementsByClassName('dashboard-cols-module');
          var total_weight = 0;

          var i;
          for(i = 0; i < elements.length; i++){
            var element=elements[i];
            var id = elements[i].id;

            var weight = componentWeight[id];
            total_weight = total_weight + weight;

            if(total_weight > 3){
              return false;
            }
          }
        }

        return !target.classList.contains('dashboard-cols-group');
      },

      copy: function (el, source) {
        return source.classList.contains('dashboard-cols-group');
      },
    });

    dragulaService.drop.subscribe((value) => {
      this.onDrop(value.slice(1));
    });
  }

  private onDrop(args) {
    //Calculate max weight allowed
    let [e, el] = args;
    if(el && el.classList){
      // el.classList.add("filled");
    }
  }
}
