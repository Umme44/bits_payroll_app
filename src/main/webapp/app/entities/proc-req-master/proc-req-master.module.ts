import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProcReqMasterComponent } from './list/proc-req-master.component';
import { ProcReqMasterDetailComponent } from './detail/proc-req-master-detail.component';
import { ProcReqMasterUpdateComponent } from './update/proc-req-master-update.component';
import { ProcReqMasterDeleteDialogComponent } from './delete/proc-req-master-delete-dialog.component';
import { ProcReqMasterRoutingModule } from './route/proc-req-master-routing.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectYearModule } from '../../shared/select-year/select-year.module';
import { BitsHrPayrollSelectMonthModule } from '../../shared/select-month/select-month.module';
import { VerticalTimelineModule } from '../../shared/verical-timeline/vertical-timeline.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';
import { ProcReqPrintFormComponent } from './print-form/proc-req-print-form.component';

@NgModule({
  imports: [
    SharedModule,
    ProcReqMasterRoutingModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSelectYearModule,
    BitsHrPayrollSelectMonthModule,
    VerticalTimelineModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [
    ProcReqMasterComponent,
    ProcReqMasterDetailComponent,
    ProcReqMasterUpdateComponent,
    ProcReqMasterDeleteDialogComponent,
    ProcReqPrintFormComponent,
  ],
})
export class ProcReqMasterModule {}
