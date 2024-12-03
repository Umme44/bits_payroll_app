import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AitConfigComponent } from '../list/ait-config.component';
import { AitConfigDetailComponent } from '../detail/ait-config-detail.component';
import { AitConfigUpdateComponent } from '../update/ait-config-update.component';
import { AitConfigRoutingResolveService } from './ait-config-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const aitConfigRoute: Routes = [
  {
    path: '',
    component: AitConfigComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AitConfigDetailComponent,
    resolve: {
      aitConfig: AitConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AitConfigUpdateComponent,
    resolve: {
      aitConfig: AitConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AitConfigUpdateComponent,
    resolve: {
      aitConfig: AitConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aitConfigRoute)],
  exports: [RouterModule],
})
export class AitConfigRoutingModule {}
