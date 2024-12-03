import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';

import { SharedModule } from '../../shared/shared.module';

import { UPCOMING_EVENT_PROBATION_END_ROUTE, UpcomingEventProbationEndComponent } from './index';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([UPCOMING_EVENT_PROBATION_END_ROUTE]), NgSelectModule, BitsHrPayrollHeaderModule],
  declarations: [UpcomingEventProbationEndComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUpcomingEventProbationEndModule {}
