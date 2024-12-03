import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../../shared/shared.module';
import { FlexScheduleApplicationUserComponent } from './flex-schedule-application-user.component';
import { FlexScheduleApplicationDetailUserModalComponent } from './flex-schedule-application-detail-user-modal.component';
import { FlexScheduleApplicationUpdateUserComponent } from './flex-schedule-application-update-user.component';
import { flexScheduleApplicationUserRoute } from './flex-schedule-application-user.route';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { BitsHrPayrollShowStatusModule } from '../../../shared/show-status/show-stauts.module';

@NgModule({
  imports: [
    RouterModule.forChild(flexScheduleApplicationUserRoute),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollShowStatusModule,
    SharedModule,
  ],
  declarations: [
    FlexScheduleApplicationUserComponent,
    FlexScheduleApplicationDetailUserModalComponent,
    FlexScheduleApplicationUpdateUserComponent,
  ],
  entryComponents: [],
})
export class BitsHrPayrollFlexScheduleApplicationUserModule {}
