import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeSalaryComponent } from '../list/employee-salary.component';
import { EmployeeSalaryDetailComponent } from '../detail/employee-salary-detail.component';
import { EmployeeSalaryUpdateComponent } from '../update/employee-salary-update.component';
import { EmployeeSalaryRoutingResolveService } from './employee-salary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeeSalaryRoute: Routes = [
  {
    path: '',
    component: EmployeeSalaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeSalaryDetailComponent,
    resolve: {
      employeeSalary: EmployeeSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeSalaryUpdateComponent,
    resolve: {
      employeeSalary: EmployeeSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeSalaryUpdateComponent,
    resolve: {
      employeeSalary: EmployeeSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':year/:month',
    component: EmployeeSalaryComponent,
    resolve: {
      employeeSalary: EmployeeSalaryRoutingResolveService,
    },
    data: {
      isSingleEmployeeSalary: false,
      defaultSort: 'pin,asc',
      pageTitle: 'bitsHrPayrollApp.employeeSalary.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':employeeId/:year/:month',
    component: EmployeeSalaryComponent,
    resolve: {
      employeeSalary: EmployeeSalaryRoutingResolveService,
    },
    data: {
      isSingleEmployeeSalary: true,
      defaultSort: 'pin,asc',
      pageTitle: 'bitsHrPayrollApp.employeeSalary.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeSalaryRoute)],
  exports: [RouterModule],
})
export class EmployeeSalaryRoutingModule {}
