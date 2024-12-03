import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { workFromHomeApplicationApprovalsRoute } from './work-from-home-application-approval.route';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../../shared/search-text-box/search-text-box.module';
import { WorkFromHomeApplicationsApprovalsLMComponent } from './work-from-home-applications-approvals-lm.component';
import { WorkFromHomeApplicationsApprovalsHRComponent } from './work-from-home-applications-approvals-hr.component';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(workFromHomeApplicationApprovalsRoute),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [WorkFromHomeApplicationsApprovalsLMComponent, WorkFromHomeApplicationsApprovalsHRComponent],
})
export class BitsHrPayrollWorkFromHomeApplicationLMApprovalsModule {}
