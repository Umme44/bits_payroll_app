import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeNOCComponent } from '../list/employee-noc.component';
import { EmployeeNOCDetailComponent } from '../detail/employee-noc-detail.component';
import { EmployeeNOCUpdateComponent } from '../update/employee-noc-update.component';
import { EmployeeNOCRoutingResolveService } from './employee-noc-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeeNOCRoute: Routes = [
  {
    path: '',
    component: EmployeeNOCComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeNOCDetailComponent,
    resolve: {
      employeeNOC: EmployeeNOCRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeNOCUpdateComponent,
    resolve: {
      employeeNOC: EmployeeNOCRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeNOCUpdateComponent,
    resolve: {
      employeeNOC: EmployeeNOCRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeNOCRoute)],
  exports: [RouterModule],
})
export class EmployeeNOCRoutingModule {}
