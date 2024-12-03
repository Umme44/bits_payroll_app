import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecruitmentRequisitionBudgetComponent } from '../list/recruitment-requisition-budget.component';
import { RecruitmentRequisitionBudgetDetailComponent } from '../detail/recruitment-requisition-budget-detail.component';
import { RecruitmentRequisitionBudgetUpdateComponent } from '../update/recruitment-requisition-budget-update.component';
import { RecruitmentRequisitionBudgetRoutingResolveService } from './recruitment-requisition-budget-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const recruitmentRequisitionBudgetRoute: Routes = [
  {
    path: '',
    component: RecruitmentRequisitionBudgetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecruitmentRequisitionBudgetDetailComponent,
    resolve: {
      recruitmentRequisitionBudget: RecruitmentRequisitionBudgetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecruitmentRequisitionBudgetUpdateComponent,
    resolve: {
      recruitmentRequisitionBudget: RecruitmentRequisitionBudgetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecruitmentRequisitionBudgetUpdateComponent,
    resolve: {
      recruitmentRequisitionBudget: RecruitmentRequisitionBudgetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recruitmentRequisitionBudgetRoute)],
  exports: [RouterModule],
})
export class RecruitmentRequisitionBudgetRoutingModule {}
