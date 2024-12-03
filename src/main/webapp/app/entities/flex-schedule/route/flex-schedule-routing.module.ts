import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FlexScheduleComponent } from '../list/flex-schedule.component';
import { ASC } from 'app/config/navigation.constants';

const flexScheduleRoute: Routes = [
  {
    path: '',
    component: FlexScheduleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: ':id/view',
  //   component: FlexScheduleDetailComponent,
  //   resolve: {
  //     flexSchedule: FlexScheduleRoutingResolveService,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'new',
  //   component: FlexScheduleUpdateComponent,
  //   resolve: {
  //     flexSchedule: FlexScheduleRoutingResolveService,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: ':id/edit',
  //   component: FlexScheduleUpdateComponent,
  //   resolve: {
  //     flexSchedule: FlexScheduleRoutingResolveService,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
];

@NgModule({
  imports: [RouterModule.forChild(flexScheduleRoute)],
  exports: [RouterModule],
})
export class FlexScheduleRoutingModule {}
