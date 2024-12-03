import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AttendanceEntryComponent } from './list/attendance-entry.component';
import { AttendanceEntryDetailComponent } from './detail/attendance-entry-detail.component';
import { AttendanceEntryUpdateComponent } from './update/attendance-entry-update.component';
import { AttendanceEntryDeleteDialogComponent } from './delete/attendance-entry-delete-dialog.component';
import { AttendanceEntryRoutingModule } from './route/attendance-entry-routing.module';
import { NgIf } from '@angular/common';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';

@NgModule({
  imports: [
    SharedModule,
    AttendanceEntryRoutingModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
  ],
  declarations: [
    AttendanceEntryComponent,
    AttendanceEntryDetailComponent,
    AttendanceEntryUpdateComponent,
    AttendanceEntryDeleteDialogComponent,
  ],
})
export class AttendanceEntryModule {}
