import { NgModule } from '@angular/core';

import { T1DetailsComponent, T1ListComponent, T1NewComponent} from './';
import { t1Route } from './t-1.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [
    T1DetailsComponent, T1ListComponent,T1NewComponent
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    t1Route,
    SharedModule,
    GeneralComponentsModule,
  ]
})
export class T1Module {
}
