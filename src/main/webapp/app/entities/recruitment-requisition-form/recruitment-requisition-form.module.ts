import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { RecruitmentRequisitionFormComponent } from './list/recruitment-requisition-form.component';
import { RecruitmentRequisitionFormDetailComponent } from './detail/recruitment-requisition-form-detail.component';
import { RecruitmentRequisitionFormUpdateComponent } from './update/recruitment-requisition-form-update.component';
import { RecruitmentRequisitionFormDeleteDialogComponent } from './delete/recruitment-requisition-form-delete-dialog.component';
import { RecruitmentRequisitionFormRoutingModule } from './route/recruitment-requisition-form-routing.module';
import {
  BitsHrPayrollSimpleSelectEmployeeFormModule
} from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [
    SharedModule,
    NgSelectModule,
    RecruitmentRequisitionFormRoutingModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule,
  ],
  declarations: [
    RecruitmentRequisitionFormComponent,
    RecruitmentRequisitionFormDetailComponent,
    RecruitmentRequisitionFormUpdateComponent,
    RecruitmentRequisitionFormDeleteDialogComponent,
  ],
})
export class RecruitmentRequisitionFormModule {}
