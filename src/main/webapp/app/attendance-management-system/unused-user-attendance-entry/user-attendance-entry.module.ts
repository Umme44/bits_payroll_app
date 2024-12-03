import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { USER_ATTENDANCE_ENTRY_ROUTE, UserAttendanceEntryComponent } from './index';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([USER_ATTENDANCE_ENTRY_ROUTE])],
  declarations: [UserAttendanceEntryComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUserAttendanceEntryModule {}
