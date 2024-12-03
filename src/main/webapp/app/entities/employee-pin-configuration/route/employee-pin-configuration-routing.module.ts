import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeePinConfigurationComponent } from '../list/employee-pin-configuration.component';
import { EmployeePinConfigurationDetailComponent } from '../detail/employee-pin-configuration-detail.component';
import { EmployeePinConfigurationUpdateComponent } from '../update/employee-pin-configuration-update.component';
import { EmployeePinConfigurationRoutingResolveService } from './employee-pin-configuration-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeePinConfigurationRoute: Routes = [
  {
    path: '',
    component: EmployeePinConfigurationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeePinConfigurationDetailComponent,
    resolve: {
      employeePinConfiguration: EmployeePinConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeePinConfigurationUpdateComponent,
    resolve: {
      employeePinConfiguration: EmployeePinConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeePinConfigurationUpdateComponent,
    resolve: {
      employeePinConfiguration: EmployeePinConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeePinConfigurationRoute)],
  exports: [RouterModule],
})
export class EmployeePinConfigurationRoutingModule {}
