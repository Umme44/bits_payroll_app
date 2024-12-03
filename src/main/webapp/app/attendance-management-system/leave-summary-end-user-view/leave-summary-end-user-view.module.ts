import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { LEAVE_SUMMARY_END_USER_VIEW_ROUTE, LeaveSummaryEndUserViewComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([LEAVE_SUMMARY_END_USER_VIEW_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [LeaveSummaryEndUserViewComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppLeaveSummaryEndUserViewModule {}
