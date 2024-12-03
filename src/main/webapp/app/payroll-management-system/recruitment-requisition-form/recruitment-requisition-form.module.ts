import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';

import { SharedModule } from 'app/shared/shared.module';
import { recruitmentRequisitionFormRoute } from './recruitment-requisition-form.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { UserRecruitmentRequisitionFormUpdateComponent } from './user/user-recruitment-requisition-form-update.component';
import { UserRecruitmentRequisitionFormComponent } from './user/user-recruitment-requisition-form.component';
import { RecruitmentRequisitionFormApprovalComponent } from './approval/recruitment-requisition-form-approval.component';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';
import { RecruitmentRequisitionFormDetailPrintViewComponent } from './approval/recruitment-requisition-form-detail-print-view.component';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { RecruitmentRequisitionFormRaiseOnBehalfUpdateComponent } from './raise-on-behalf/recruitment-requisition-form-raise-on-behalf-update.component';
import { RecruitmentRequisitionFormRaiseOnBehalfComponent } from './raise-on-behalf/recruitment-requisition-form-raise-on-behalf.component';
import { RecruitmentRequisitionFormDetailComponent } from './recruitment-requisition-form-detail.component';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(recruitmentRequisitionFormRoute),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    NgSelectModule,
  ],
  declarations: [
    UserRecruitmentRequisitionFormComponent,
    RecruitmentRequisitionFormDetailComponent,
    UserRecruitmentRequisitionFormUpdateComponent,
    RecruitmentRequisitionFormApprovalComponent,
    RecruitmentRequisitionFormDetailPrintViewComponent,
    RecruitmentRequisitionFormRaiseOnBehalfUpdateComponent,
    RecruitmentRequisitionFormRaiseOnBehalfComponent,
  ],
  entryComponents: [UserRecruitmentRequisitionFormComponent],
})
export class BitsHrPayrollRecruitmentRequisitionFormModule {}
