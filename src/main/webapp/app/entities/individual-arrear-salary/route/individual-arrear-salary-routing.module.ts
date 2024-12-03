import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IndividualArrearSalaryComponent } from '../list/individual-arrear-salary.component';
import { IndividualArrearSalaryDetailComponent } from '../detail/individual-arrear-salary-detail.component';
import { IndividualArrearSalaryUpdateComponent } from '../update/individual-arrear-salary-update.component';
import { IndividualArrearSalaryRoutingResolveService } from './individual-arrear-salary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { IndividualArrearSameGroupSalariesComponent } from '../detailed-list-view/individual-arrear-same-group-salaries.component';

const individualArrearSalaryRoute: Routes = [
  {
    path: '',
    component: IndividualArrearSalaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IndividualArrearSalaryDetailComponent,
    resolve: {
      individualArrearSalary: IndividualArrearSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'title/:title/view',
    component: IndividualArrearSameGroupSalariesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IndividualArrearSalaryUpdateComponent,
    resolve: {
      individualArrearSalary: IndividualArrearSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IndividualArrearSalaryUpdateComponent,
    resolve: {
      individualArrearSalary: IndividualArrearSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(individualArrearSalaryRoute)],
  exports: [RouterModule],
})
export class IndividualArrearSalaryRoutingModule {}
