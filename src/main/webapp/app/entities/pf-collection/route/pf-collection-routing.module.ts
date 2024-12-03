import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PfCollectionComponent } from '../list/pf-collection.component';
import { PfCollectionDetailComponent } from '../detail/pf-collection-detail.component';
import { PfCollectionUpdateComponent } from '../update/pf-collection-update.component';
import { PfCollectionRoutingResolveService } from './pf-collection-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pfCollectionRoute: Routes = [
  {
    path: '',
    component: PfCollectionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfCollectionDetailComponent,
    resolve: {
      pfCollection: PfCollectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfCollectionUpdateComponent,
    resolve: {
      pfCollection: PfCollectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfCollectionUpdateComponent,
    resolve: {
      pfCollection: PfCollectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pfCollectionRoute)],
  exports: [RouterModule],
})
export class PfCollectionRoutingModule {}
