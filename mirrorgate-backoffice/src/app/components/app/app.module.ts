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

import { NgModule } from '@angular/core'
import { RouterModule } from '@angular/router';
import { rootRouterConfig } from './app.routes';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { TagInputModule } from 'ngx-chips';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { ListComponent } from '../list/list.component';
import { FormComponent } from '../form/form.component';
import { DeleteComponent } from '../delete/delete.component';
import { FeedbackComponent } from '../feedback/feedback.component';

import { LocationStrategy, HashLocationStrategy } from '@angular/common';

import { DragulaModule, DragulaService } from 'ng2-dragula';

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
      copy: true,
      copySortSource: false,

      accepts: function(el, target) {
        return target && target.classList ? !target.classList.contains('filled') : false;
      },
    });

    dragulaService.drop.subscribe((value) => {
      console.log(`drop: ${value[0]}`);
      this.onDrop(value.slice(1));
    });
  }
  
  private onDrop(args) {
    let [e, el] = args;
    if(el && el.classList){
      el.classList.add("filled");
    }
  }
}
