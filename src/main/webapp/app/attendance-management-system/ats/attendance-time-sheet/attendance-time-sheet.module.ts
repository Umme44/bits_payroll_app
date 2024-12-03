import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../../shared/shared.module';

import { ATTENDANCE_TIME_SHEET_ROUTE, AttendanceTimeSheetComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([ATTENDANCE_TIME_SHEET_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [AttendanceTimeSheetComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppAttendanceTimeSheetModule {}
