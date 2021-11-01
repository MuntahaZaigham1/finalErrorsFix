import { NgModule } from '@angular/core';

import { T2DetailsComponent, T2ListComponent, T2NewComponent} from './';
import { t2Route } from './t-2.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [
    T2DetailsComponent, T2ListComponent,T2NewComponent
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    t2Route,
    SharedModule,
    GeneralComponentsModule,
  ]
})
export class T2Module {
}
