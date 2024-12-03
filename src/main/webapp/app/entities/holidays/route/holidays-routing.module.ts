import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HolidaysComponent } from '../list/holidays.component';
import { HolidaysDetailComponent } from '../detail/holidays-detail.component';
import { HolidaysUpdateComponent } from '../update/holidays-update.component';
import { HolidaysRoutingResolveService } from './holidays-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const holidaysRoute: Routes = [
  {
    path: '',
    component: HolidaysComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HolidaysDetailComponent,
    resolve: {
      holidays: HolidaysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HolidaysUpdateComponent,
    resolve: {
      holidays: HolidaysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HolidaysUpdateComponent,
    resolve: {
      holidays: HolidaysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(holidaysRoute)],
  exports: [RouterModule],
})
export class HolidaysRoutingModule {}
