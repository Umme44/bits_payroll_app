import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecruitmentRequisitionBudgetComponent } from './list/recruitment-requisition-budget.component';
import { RecruitmentRequisitionBudgetDetailComponent } from './detail/recruitment-requisition-budget-detail.component';
import { RecruitmentRequisitionBudgetUpdateComponent } from './update/recruitment-requisition-budget-update.component';
import { RecruitmentRequisitionBudgetDeleteDialogComponent } from './delete/recruitment-requisition-budget-delete-dialog.component';
import { RecruitmentRequisitionBudgetRoutingModule } from './route/recruitment-requisition-budget-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RecruitmentRequisitionBudgetRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    RecruitmentRequisitionBudgetComponent,
    RecruitmentRequisitionBudgetDetailComponent,
    RecruitmentRequisitionBudgetUpdateComponent,
    RecruitmentRequisitionBudgetDeleteDialogComponent,
  ],
})
export class RecruitmentRequisitionBudgetModule {}
