import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { AppDetailsRoutingModule } from '../app-details/app-details-routing.module';
import { MaterialModule } from '../material.module';
import { MainRoutingModule } from './basic-routing.module';
import { HomePageComponent } from './home-page/home-page.component';
import { MenuComponent } from './menu/menu.component';
import { NavbarComponent } from './navbar/navbar.component';


@NgModule({
    imports: [
        CommonModule,
        MainRoutingModule,
        MaterialModule,
        AppDetailsRoutingModule
    ],
    declarations: [
        HomePageComponent,
        MenuComponent,
        NavbarComponent
    ],
    exports: [
        MenuComponent,
        NavbarComponent
    ]
})
export class MainModule { }
