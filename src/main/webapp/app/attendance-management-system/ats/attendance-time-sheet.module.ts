import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BitsHrPayrollSelectEmployeeFormModule } from 'app/shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';
import { ATSAdminManualAttendanceEntryDialogComponent } from './attendance-time-sheet-admin/ats-admin-manual-attendance-entry-update-dialog.component';
import { AtsAdminLeaveApplicationDialogComponent } from 'app/attendance-management-system/ats/attendance-time-sheet-admin/ats-admin-leave-application-dialog.component';

@NgModule({
  imports: [SharedModule, BitsHrPayrollSelectEmployeeFormModule, BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [ATSAdminManualAttendanceEntryDialogComponent, AtsAdminLeaveApplicationDialogComponent],
  entryComponents: [],
})
export class BitsHrPayrollAttendanceTimeSheetModule {}
