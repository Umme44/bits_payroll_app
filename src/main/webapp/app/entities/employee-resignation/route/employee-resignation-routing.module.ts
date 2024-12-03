import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeResignationComponent } from '../list/employee-resignation.component';
import { EmployeeResignationDetailComponent } from '../detail/employee-resignation-detail.component';
import { EmployeeResignationUpdateComponent } from '../update/employee-resignation-update.component';
import { EmployeeResignationRoutingResolveService } from './employee-resignation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import {EmployeeResignationRejectComponent} from "../reject/employee-resignation-reject.component";
import {EmployeeResignationApproveComponent} from "../approve/employee-resignation-approve.component";

const employeeResignationRoute: Routes = [
  {
    path: '',
    component: EmployeeResignationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeResignationDetailComponent,
    resolve: {
      employeeResignation: EmployeeResignationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeResignationUpdateComponent,
    resolve: {
      employeeResignation: EmployeeResignationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeResignationUpdateComponent,
    resolve: {
      employeeResignation: EmployeeResignationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/reject',
    component: EmployeeResignationRejectComponent,
    resolve: {
      employeeResignation: EmployeeResignationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/approve',
    component: EmployeeResignationApproveComponent,
    resolve: {
      employeeResignation: EmployeeResignationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeResignationRoute)],
  exports: [RouterModule],
})
export class EmployeeResignationRoutingModule {}
