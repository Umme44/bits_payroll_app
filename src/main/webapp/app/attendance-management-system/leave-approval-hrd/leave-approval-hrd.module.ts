import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { LEAVE_APPROVAL_HRD_ROUTE, LeaveApprovalHrdComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from 'app/shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([LEAVE_APPROVAL_HRD_ROUTE]), BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [LeaveApprovalHrdComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppLeaveApprovalHrdModule {}
