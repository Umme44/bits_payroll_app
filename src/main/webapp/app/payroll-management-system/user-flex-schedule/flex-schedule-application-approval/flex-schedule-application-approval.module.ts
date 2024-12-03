import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { FlexScheduleApplicationApprovalComponent } from './flex-schedule-application-approval.component';
import { FlexScheduleApplicationApprovedByMeComponent } from './flex-schedule-application-approved-by-me.component';
import { BitsHrPayrollSearchTextBoxModule } from '../../../shared/search-text-box/search-text-box.module';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { FLEX_SCHEDULE_APPLICATION_APPROVAL_ROUTE } from './flex-schedule-application-approval.route';
import { SharedModule } from '../../../shared/shared.module';

import { NgSelectModule } from '@ng-select/ng-select';
import { FlexScheduleApplicationDetailApprovalModalComponent } from './flex-schedule-application-detail-approval-modal.component';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(FLEX_SCHEDULE_APPLICATION_APPROVAL_ROUTE),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
    NgSelectModule,
    SharedModule,
  ],
  declarations: [
    FlexScheduleApplicationApprovalComponent,
    FlexScheduleApplicationApprovedByMeComponent,
    FlexScheduleApplicationDetailApprovalModalComponent,
  ],
})
export class BitsHrPayrollAppFlexScheduleApplicationApprovalModule {}
