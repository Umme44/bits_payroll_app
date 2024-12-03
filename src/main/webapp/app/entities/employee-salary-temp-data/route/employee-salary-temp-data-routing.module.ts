import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeSalaryTempDataComponent } from '../list/employee-salary-temp-data.component';
import { EmployeeSalaryTempDataDetailComponent } from '../detail/employee-salary-temp-data-detail.component';
import { EmployeeSalaryTempDataUpdateComponent } from '../update/employee-salary-temp-data-update.component';
import { EmployeeSalaryTempDataRoutingResolveService } from './employee-salary-temp-data-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeeSalaryTempDataRoute: Routes = [
  {
    path: '',
    component: EmployeeSalaryTempDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeSalaryTempDataDetailComponent,
    resolve: {
      employeeSalaryTempData: EmployeeSalaryTempDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeSalaryTempDataUpdateComponent,
    resolve: {
      employeeSalaryTempData: EmployeeSalaryTempDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeSalaryTempDataUpdateComponent,
    resolve: {
      employeeSalaryTempData: EmployeeSalaryTempDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeSalaryTempDataRoute)],
  exports: [RouterModule],
})
export class EmployeeSalaryTempDataRoutingModule {}
