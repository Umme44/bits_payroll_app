import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProcReqComponent } from './list/proc-req.component';
import { ProcReqDetailComponent } from './detail/proc-req-detail.component';
import { ProcReqUpdateComponent } from './update/proc-req-update.component';
import { ProcReqDeleteDialogComponent } from './delete/proc-req-delete-dialog.component';
import { ProcReqRoutingModule } from './route/proc-req-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { ProcReqItemUpdateDialogComponent } from './update/proc-req-item-update-dialog.component';
import { VerticalTimelineModule } from '../../shared/verical-timeline/vertical-timeline.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { ProcReqPrintFormComponent } from './print-form/proc-req-print-form.component';
import { ProcurementRequisitionApprovalComponent } from './approval/procurement-requisition-approval.component';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [
    SharedModule,
    ProcReqRoutingModule,
    BitsHrPayrollHeaderModule,
    VerticalTimelineModule,
    NgSelectModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [
    ProcReqComponent,
    ProcReqDetailComponent,
    ProcReqUpdateComponent,
    ProcReqDeleteDialogComponent,
    ProcReqItemUpdateDialogComponent,
    ProcReqPrintFormComponent,
    ProcurementRequisitionApprovalComponent,
  ],
})
export class ProcReqModule {}
