import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SalaryGeneratorMasterComponent } from '../list/salary-generator-master.component';
import { SalaryGeneratorMasterDetailComponent } from '../detail/salary-generator-master-detail.component';
import { SalaryGeneratorMasterUpdateComponent } from '../update/salary-generator-master-update.component';
import { SalaryGeneratorMasterRoutingResolveService } from './salary-generator-master-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from '../../../config/authority.constants';
import { HoldSalariesApprovalComponent } from '../hold-salaries-approval/hold-salaries-approval.component';
import { SalaryLockComponent } from '../salary-lock/salary-lock.component';

const salaryGeneratorMasterRoute: Routes = [
  {
    path: '',
    component: SalaryGeneratorMasterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalaryGeneratorMasterDetailComponent,
    resolve: {
      salaryGeneratorMaster: SalaryGeneratorMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalaryGeneratorMasterUpdateComponent,
    resolve: {
      salaryGeneratorMaster: SalaryGeneratorMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalaryGeneratorMasterUpdateComponent,
    resolve: {
      salaryGeneratorMaster: SalaryGeneratorMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'lock',
    component: SalaryLockComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.salaryLock.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'hold',
    component: HoldSalariesApprovalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.salaryHold.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(salaryGeneratorMasterRoute)],
  exports: [RouterModule],
})
export class SalaryGeneratorMasterRoutingModule {}
