import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../../shared/shared.module';
import { MONTHLY_ATTENDANCE_TIME_SHEET_ROUTE, MonthlyAttendanceTimeSheetComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { PreRegularizeModalComponent } from 'app/attendance-management-system/ats/monthly-attendance-time-sheet/pre-regularize-modal.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([MONTHLY_ATTENDANCE_TIME_SHEET_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [MonthlyAttendanceTimeSheetComponent, PreRegularizeModalComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppMonthlyAttendanceTimeSheetModule {}
