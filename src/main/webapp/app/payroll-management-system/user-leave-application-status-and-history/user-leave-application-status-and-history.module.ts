import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { USER_LEAVE_APPLICATION_STATUS_AND_HISTORY_ROUTE, UserLeaveApplicationStatusAndHistoryComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([USER_LEAVE_APPLICATION_STATUS_AND_HISTORY_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [UserLeaveApplicationStatusAndHistoryComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUserLeaveApplicationStatusAndHistoryModule {}
