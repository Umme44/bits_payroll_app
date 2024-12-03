import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';

import { SharedModule } from '../../shared/shared.module';

import { UPCOMING_EVENT_CONTRACT_END_ROUTE, UpcomingEventContractEndComponent } from './index';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([UPCOMING_EVENT_CONTRACT_END_ROUTE]), NgSelectModule, BitsHrPayrollHeaderModule],
  declarations: [UpcomingEventContractEndComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUpcomingEventContractEndModule {}
