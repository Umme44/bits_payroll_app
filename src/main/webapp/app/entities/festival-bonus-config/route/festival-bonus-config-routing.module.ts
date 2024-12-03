import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FestivalBonusConfigComponent } from '../list/festival-bonus-config.component';
import { FestivalBonusConfigDetailComponent } from '../detail/festival-bonus-config-detail.component';
import { FestivalBonusConfigUpdateComponent } from '../update/festival-bonus-config-update.component';
import { FestivalBonusConfigRoutingResolveService } from './festival-bonus-config-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const festivalBonusConfigRoute: Routes = [
  {
    path: '',
    component: FestivalBonusConfigComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FestivalBonusConfigDetailComponent,
    resolve: {
      festivalBonusConfig: FestivalBonusConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FestivalBonusConfigUpdateComponent,
    resolve: {
      festivalBonusConfig: FestivalBonusConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FestivalBonusConfigUpdateComponent,
    resolve: {
      festivalBonusConfig: FestivalBonusConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(festivalBonusConfigRoute)],
  exports: [RouterModule],
})
export class FestivalBonusConfigRoutingModule {}
