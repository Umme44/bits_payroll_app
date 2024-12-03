import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AttendanceSummaryComponent } from './list/attendance-summary.component';
import { AttendanceSummaryDetailComponent } from './detail/attendance-summary-detail.component';
import { AttendanceSummaryUpdateComponent } from './update/attendance-summary-update.component';
import { AttendanceSummaryDeleteDialogComponent } from './delete/attendance-summary-delete-dialog.component';
import { AttendanceSummaryRoutingModule } from './route/attendance-summary-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [
    SharedModule,
    AttendanceSummaryRoutingModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [
    AttendanceSummaryComponent,
    AttendanceSummaryDetailComponent,
    AttendanceSummaryUpdateComponent,
    AttendanceSummaryDeleteDialogComponent,
  ],
})
export class AttendanceSummaryModule {}
