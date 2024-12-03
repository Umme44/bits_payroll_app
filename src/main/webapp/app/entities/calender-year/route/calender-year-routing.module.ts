import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CalenderYearComponent } from '../list/calender-year.component';
import { CalenderYearDetailComponent } from '../detail/calender-year-detail.component';
import { CalenderYearUpdateComponent } from '../update/calender-year-update.component';
import { CalenderYearRoutingResolveService } from './calender-year-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const calenderYearRoute: Routes = [
  {
    path: '',
    component: CalenderYearComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CalenderYearDetailComponent,
    resolve: {
      calenderYear: CalenderYearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CalenderYearUpdateComponent,
    resolve: {
      calenderYear: CalenderYearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CalenderYearUpdateComponent,
    resolve: {
      calenderYear: CalenderYearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(calenderYearRoute)],
  exports: [RouterModule],
})
export class CalenderYearRoutingModule {}
