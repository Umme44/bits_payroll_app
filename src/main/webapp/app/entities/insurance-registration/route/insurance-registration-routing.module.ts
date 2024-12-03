import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InsuranceRegistrationComponent } from '../list/insurance-registration.component';
import { InsuranceRegistrationDetailComponent } from '../detail/insurance-registration-detail.component';
import { InsuranceRegistrationUpdateComponent } from '../update/insurance-registration-update.component';
import { InsuranceRegistrationRoutingResolveService } from './insurance-registration-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const insuranceRegistrationRoute: Routes = [
  {
    path: '',
    component: InsuranceRegistrationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InsuranceRegistrationDetailComponent,
    resolve: {
      insuranceRegistration: InsuranceRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InsuranceRegistrationUpdateComponent,
    resolve: {
      insuranceRegistration: InsuranceRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InsuranceRegistrationUpdateComponent,
    resolve: {
      insuranceRegistration: InsuranceRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(insuranceRegistrationRoute)],
  exports: [RouterModule],
})
export class InsuranceRegistrationRoutingModule {}
