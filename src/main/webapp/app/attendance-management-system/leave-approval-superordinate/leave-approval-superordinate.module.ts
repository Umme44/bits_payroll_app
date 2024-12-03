import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { LEAVE_APPROVAL_SUPERORDINATE_ROUTE, LeaveApprovalSuperordinateComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from 'app/shared/search-text-box/search-text-box.module';
import { LeaveApprovedByMeComponent } from 'app/attendance-management-system/leave-approval-superordinate/leave-approved-by-me.component';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(LEAVE_APPROVAL_SUPERORDINATE_ROUTE),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [LeaveApprovalSuperordinateComponent, LeaveApprovedByMeComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppLeaveApprovalSuperordinateModule {}
