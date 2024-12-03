import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecruitmentRequisitionFormComponent } from '../list/recruitment-requisition-form.component';
import { RecruitmentRequisitionFormDetailComponent } from '../detail/recruitment-requisition-form-detail.component';
import { RecruitmentRequisitionFormUpdateComponent } from '../update/recruitment-requisition-form-update.component';
import { RecruitmentRequisitionFormRoutingResolveService } from './recruitment-requisition-form-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const recruitmentRequisitionFormRoute: Routes = [
  {
    path: '',
    component: RecruitmentRequisitionFormComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecruitmentRequisitionFormDetailComponent,
    resolve: {
      recruitmentRequisitionForm: RecruitmentRequisitionFormRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecruitmentRequisitionFormUpdateComponent,
    resolve: {
      recruitmentRequisitionForm: RecruitmentRequisitionFormRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecruitmentRequisitionFormUpdateComponent,
    resolve: {
      recruitmentRequisitionForm: RecruitmentRequisitionFormRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recruitmentRequisitionFormRoute)],
  exports: [RouterModule],
})
export class RecruitmentRequisitionFormRoutingModule {}
