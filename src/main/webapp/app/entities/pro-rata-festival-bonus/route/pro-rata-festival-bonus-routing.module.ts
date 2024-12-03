import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProRataFestivalBonusComponent } from '../list/pro-rata-festival-bonus.component';
import { ProRataFestivalBonusDetailComponent } from '../detail/pro-rata-festival-bonus-detail.component';
import { ProRataFestivalBonusUpdateComponent } from '../update/pro-rata-festival-bonus-update.component';
import { ProRataFestivalBonusRoutingResolveService } from './pro-rata-festival-bonus-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const proRataFestivalBonusRoute: Routes = [
  {
    path: '',
    component: ProRataFestivalBonusComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProRataFestivalBonusDetailComponent,
    resolve: {
      proRataFestivalBonus: ProRataFestivalBonusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProRataFestivalBonusUpdateComponent,
    resolve: {
      proRataFestivalBonus: ProRataFestivalBonusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProRataFestivalBonusUpdateComponent,
    resolve: {
      proRataFestivalBonus: ProRataFestivalBonusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(proRataFestivalBonusRoute)],
  exports: [RouterModule],
})
export class ProRataFestivalBonusRoutingModule {}
