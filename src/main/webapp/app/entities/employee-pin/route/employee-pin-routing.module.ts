import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeePinComponent } from '../list/employee-pin.component';
import { EmployeePinDetailComponent } from '../detail/employee-pin-detail.component';
import { EmployeePinUpdateComponent } from '../update/employee-pin-update.component';
import { EmployeePinRoutingResolveService } from './employee-pin-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeePinRoute: Routes = [
  {
    path: '',
    component: EmployeePinComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeePinDetailComponent,
    resolve: {
      employeePin: EmployeePinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeePinUpdateComponent,
    resolve: {
      employeePin: EmployeePinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeePinUpdateComponent,
    resolve: {
      employeePin: EmployeePinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeePinRoute)],
  exports: [RouterModule],
})
export class EmployeePinRoutingModule {}
