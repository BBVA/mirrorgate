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

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { DeleteComponent } from '../delete/delete.component';
import { DragulaModule } from 'ng2-dragula';
import { FeedbackComponent } from '../feedback/feedback.component';
import { FormComponent } from '../form/form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ListComponent } from '../list/list.component';
import { NgModule } from '@angular/core';
import { rootRouterConfig } from './app.routes';
import { RouterModule } from '@angular/router';
import { TagInputModule } from 'ngx-chips';

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
    HttpClientModule,
    RouterModule.forRoot(rootRouterConfig, { useHash: true }),
    TagInputModule,
    BrowserAnimationsModule,
    DragulaModule.forRoot()
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {

}
