import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FlexScheduleApplicationComponent } from '../list/flex-schedule-application.component';
import { FlexScheduleApplicationDetailComponent } from '../detail/flex-schedule-application-detail.component';
import { FlexScheduleApplicationUpdateComponent } from '../update/flex-schedule-application-update.component';
import { FlexScheduleApplicationRoutingResolveService } from './flex-schedule-application-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const flexScheduleApplicationRoute: Routes = [
  {
    path: '',
    component: FlexScheduleApplicationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FlexScheduleApplicationDetailComponent,
    resolve: {
      flexScheduleApplication: FlexScheduleApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FlexScheduleApplicationUpdateComponent,
    resolve: {
      flexScheduleApplication: FlexScheduleApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FlexScheduleApplicationUpdateComponent,
    resolve: {
      flexScheduleApplication: FlexScheduleApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(flexScheduleApplicationRoute)],
  exports: [RouterModule],
})
export class FlexScheduleApplicationRoutingModule {}
