import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SalaryDeductionComponent } from '../list/salary-deduction.component';
import { SalaryDeductionDetailComponent } from '../detail/salary-deduction-detail.component';
import { SalaryDeductionUpdateComponent } from '../update/salary-deduction-update.component';
import { SalaryDeductionRoutingResolveService } from './salary-deduction-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from '../../../config/authority.constants';

const salaryDeductionRoute: Routes = [
  {
    path: '',
    component: SalaryDeductionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalaryDeductionDetailComponent,
    resolve: {
      salaryDeduction: SalaryDeductionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalaryDeductionUpdateComponent,
    resolve: {
      salaryDeduction: SalaryDeductionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalaryDeductionUpdateComponent,
    resolve: {
      salaryDeduction: SalaryDeductionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':year/:month',
    component: SalaryDeductionComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.salaryDeduction.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':year/:month',
    component: SalaryDeductionComponent,
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.salaryDeduction.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(salaryDeductionRoute)],
  exports: [RouterModule],
})
export class SalaryDeductionRoutingModule {}
