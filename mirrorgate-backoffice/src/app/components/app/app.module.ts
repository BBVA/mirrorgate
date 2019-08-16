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

import { NgModule, APP_INITIALIZER } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { DragulaModule } from 'ng2-dragula';

import { ConfigService } from '../../services/config.service';

import { rootRouterConfig } from './app.routes';

import { AppComponent } from './app.component';
import { ListComponent } from '../list/list.component';
import { FormComponent } from '../form/form.component';
import { DeleteComponent } from '../delete/delete.component';
import { FeedbackComponent } from '../feedback/feedback.component';

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
    BrowserAnimationsModule,
    DragulaModule.forRoot(),
    MatFormFieldModule,
    MatChipsModule,
    MatAutocompleteModule,
    DragDropModule
  ],
  providers: [
    ConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: (config: ConfigService) => () => config.loadConfig(),
      deps: [ConfigService],
      multi: true
    }
  ],

  bootstrap: [AppComponent]
})
export class AppModule {}
