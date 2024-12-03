import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrganizationUpdateComponent } from '../update/organization-update.component';
import { ASC } from 'app/config/navigation.constants';

const organizationRoute: Routes = [
  {
    path: '',
    component: OrganizationUpdateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: ':id/view',
  //   component: OrganizationDetailComponent,
  //   resolve: {
  //     organization: OrganizationRoutingResolveService,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'new',
  //   component: OrganizationUpdateComponent,
  //   resolve: {
  //     organization: OrganizationRoutingResolveService,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: ':id/edit',
  //   component: OrganizationUpdateComponent,
  //   resolve: {
  //     organization: OrganizationRoutingResolveService,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
];

@NgModule({
  imports: [RouterModule.forChild(organizationRoute)],
  exports: [RouterModule],
})
export class OrganizationRoutingModule {}
