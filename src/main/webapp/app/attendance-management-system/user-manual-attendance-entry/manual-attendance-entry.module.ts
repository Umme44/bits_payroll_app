import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';
import { AttendanceApprovalComponent } from './manual-attendance-approval/attendance-approval.component';
import { ManualAttendanceEntryDetailComponent } from './manual-attendance-entry-detail.component';
import { ManualAttendanceEntryUpdateComponent } from './manual-attendance-entry-update.component';
import { manualAttendanceEntryUserRoute } from './manual-attendance-entry-user.route';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(manualAttendanceEntryUserRoute),
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [AttendanceApprovalComponent, ManualAttendanceEntryDetailComponent, ManualAttendanceEntryUpdateComponent],
  entryComponents: [],
})
export class BitsHrPayrollManualAttendanceEntryUserModule {}
