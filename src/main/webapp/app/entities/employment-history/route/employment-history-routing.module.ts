import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmploymentHistoryComponent } from '../list/employment-history.component';
import { EmploymentHistoryDetailComponent } from '../detail/employment-history-detail.component';
import { EmploymentHistoryUpdateComponent } from '../update/employment-history-update.component';
import { EmploymentHistoryRoutingResolveService } from './employment-history-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employmentHistoryRoute: Routes = [
  {
    path: '',
    component: EmploymentHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmploymentHistoryDetailComponent,
    resolve: {
      employmentHistory: EmploymentHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmploymentHistoryUpdateComponent,
    resolve: {
      employmentHistory: EmploymentHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmploymentHistoryUpdateComponent,
    resolve: {
      employmentHistory: EmploymentHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employmentHistoryRoute)],
  exports: [RouterModule],
})
export class EmploymentHistoryRoutingModule {}
