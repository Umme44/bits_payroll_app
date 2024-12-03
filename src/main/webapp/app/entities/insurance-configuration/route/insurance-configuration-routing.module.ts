import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InsuranceConfigurationComponent } from '../list/insurance-configuration.component';
import { InsuranceConfigurationDetailComponent } from '../detail/insurance-configuration-detail.component';
import { InsuranceConfigurationUpdateComponent } from '../update/insurance-configuration-update.component';
import { InsuranceConfigurationRoutingResolveService } from './insurance-configuration-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const insuranceConfigurationRoute: Routes = [
  {
    path: '',
    component: InsuranceConfigurationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InsuranceConfigurationDetailComponent,
    resolve: {
      insuranceConfiguration: InsuranceConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InsuranceConfigurationUpdateComponent,
    resolve: {
      insuranceConfiguration: InsuranceConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InsuranceConfigurationUpdateComponent,
    resolve: {
      insuranceConfiguration: InsuranceConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(insuranceConfigurationRoute)],
  exports: [RouterModule],
})
export class InsuranceConfigurationRoutingModule {}
