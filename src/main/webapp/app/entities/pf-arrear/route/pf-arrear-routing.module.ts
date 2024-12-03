import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PfArrearComponent } from '../list/pf-arrear.component';
import { PfArrearDetailComponent } from '../detail/pf-arrear-detail.component';
import { PfArrearUpdateComponent } from '../update/pf-arrear-update.component';
import { PfArrearRoutingResolveService } from './pf-arrear-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pfArrearRoute: Routes = [
  {
    path: '',
    component: PfArrearComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfArrearDetailComponent,
    resolve: {
      pfArrear: PfArrearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfArrearUpdateComponent,
    resolve: {
      pfArrear: PfArrearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfArrearUpdateComponent,
    resolve: {
      pfArrear: PfArrearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pfArrearRoute)],
  exports: [RouterModule],
})
export class PfArrearRoutingModule {}
