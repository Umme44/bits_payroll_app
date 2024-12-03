import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BandComponent } from '../list/band.component';
import { BandDetailComponent } from '../detail/band-detail.component';
import { BandUpdateComponent } from '../update/band-update.component';
import { BandRoutingResolveService } from './band-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bandRoute: Routes = [
  {
    path: '',
    component: BandComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BandDetailComponent,
    resolve: {
      band: BandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BandUpdateComponent,
    resolve: {
      band: BandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BandUpdateComponent,
    resolve: {
      band: BandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bandRoute)],
  exports: [RouterModule],
})
export class BandRoutingModule {}
