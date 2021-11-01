import { NgModule } from '@angular/core';

import { T2ExtendedService, T2DetailsExtendedComponent, T2ListExtendedComponent, T2NewExtendedComponent } from './';
import { T2Service } from 'src/app/entities/t-2';
import { T2Module } from 'src/app/entities/t-2/t-2.module';
import { t2Route } from './t-2.route';

import { SharedModule  } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [
    T2DetailsExtendedComponent, T2ListExtendedComponent, T2NewExtendedComponent 
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    t2Route,
    T2Module,
    SharedModule,
    GeneralComponentsExtendedModule,
  ],
  providers: [{ provide: T2Service, useClass: T2ExtendedService }],
})
export class T2ExtendedModule {
}
