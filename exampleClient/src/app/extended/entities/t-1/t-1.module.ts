import { NgModule } from '@angular/core';

import { T1ExtendedService, T1DetailsExtendedComponent, T1ListExtendedComponent, T1NewExtendedComponent } from './';
import { T1Service } from 'src/app/entities/t-1';
import { T1Module } from 'src/app/entities/t-1/t-1.module';
import { t1Route } from './t-1.route';

import { SharedModule  } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [
    T1DetailsExtendedComponent, T1ListExtendedComponent, T1NewExtendedComponent 
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    t1Route,
    T1Module,
    SharedModule,
    GeneralComponentsExtendedModule,
  ],
  providers: [{ provide: T1Service, useClass: T1ExtendedService }],
})
export class T1ExtendedModule {
}
