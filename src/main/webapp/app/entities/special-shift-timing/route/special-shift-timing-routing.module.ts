import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpecialShiftTimingComponent } from '../list/special-shift-timing.component';
import { SpecialShiftTimingDetailComponent } from '../detail/special-shift-timing-detail.component';
import { SpecialShiftTimingUpdateComponent } from '../update/special-shift-timing-update.component';
import { SpecialShiftTimingRoutingResolveService } from './special-shift-timing-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const specialShiftTimingRoute: Routes = [
  {
    path: '',
    component: SpecialShiftTimingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecialShiftTimingDetailComponent,
    resolve: {
      specialShiftTiming: SpecialShiftTimingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecialShiftTimingUpdateComponent,
    resolve: {
      specialShiftTiming: SpecialShiftTimingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecialShiftTimingUpdateComponent,
    resolve: {
      specialShiftTiming: SpecialShiftTimingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(specialShiftTimingRoute)],
  exports: [RouterModule],
})
export class SpecialShiftTimingRoutingModule {}
