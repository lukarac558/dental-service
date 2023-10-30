import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import localePl from '@angular/common/locales/pl';
import { LOCALE_ID, NgModule } from '@angular/core';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CalendarDateFormatter, CalendarModule, CalendarMomentDateFormatter, DateAdapter, MOMENT } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/moment';
import { OAuthModule } from 'angular-oauth2-oidc';
import * as moment from 'moment';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainModule } from './basic/basic.module';
import { MaterialModule } from './material.module';
import { Paginator } from './shared/components/paginator/paginator.component';
import { SpinnerModule } from './shared/components/spinner/spinner.module';

registerLocaleData(localePl);

export function momentAdapterFactory() {
    return adapterFactory(moment);
}

moment.updateLocale('pl', {
    week: {
        dow: 1,
        doy: 0
    }
});

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
        CalendarModule.forRoot({
            provide: DateAdapter,
            useFactory: momentAdapterFactory,
        }, {
            dateFormatter: {
                provide: CalendarDateFormatter,
                useClass: CalendarMomentDateFormatter,
            },
        })
    ],
    providers: [
        { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
        { provide: MatPaginatorIntl, useClass: Paginator },
        { provide: LOCALE_ID, useValue: 'pl-PL' },
        { provide: MOMENT, useValue: moment }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
