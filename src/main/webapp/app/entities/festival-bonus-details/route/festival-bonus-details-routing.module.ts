import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FestivalBonusDetailsComponent } from '../list/festival-bonus-details.component';
import { FestivalBonusDetailsDetailComponent } from '../detail/festival-bonus-details-detail.component';
import { FestivalBonusDetailsUpdateComponent } from '../update/festival-bonus-details-update.component';
import { FestivalBonusDetailsRoutingResolveService } from './festival-bonus-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const festivalBonusDetailsRoute: Routes = [
  {
    path: '',
    component: FestivalBonusDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FestivalBonusDetailsDetailComponent,
    resolve: {
      festivalBonusDetails: FestivalBonusDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FestivalBonusDetailsUpdateComponent,
    resolve: {
      festivalBonusDetails: FestivalBonusDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FestivalBonusDetailsUpdateComponent,
    resolve: {
      festivalBonusDetails: FestivalBonusDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(festivalBonusDetailsRoute)],
  exports: [RouterModule],
})
export class FestivalBonusDetailsRoutingModule {}
