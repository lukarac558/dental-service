import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import localePl from '@angular/common/locales/pl';
import { LOCALE_ID, NgModule } from '@angular/core';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { OAuthModule } from 'angular-oauth2-oidc';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainModule } from './basic/basic.module';
import { MaterialModule } from './material.module';
import { Paginator } from './shared/components/paginator/paginator.component';
import { SpinnerModule } from './shared/components/spinner/spinner.module';

registerLocaleData(localePl);

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MainModule,
        MaterialModule,
        ToastrModule.forRoot(),
        SpinnerModule,
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: ['http://localhost:8080/api'],
                sendAccessToken: true
            }
        }),
    ],
    providers: [
        { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
        { provide: MatPaginatorIntl, useClass: Paginator },
        { provide: LOCALE_ID, useValue: 'pl-PL' }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
