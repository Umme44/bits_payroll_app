import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttendanceSyncCacheComponent } from '../list/attendance-sync-cache.component';
import { AttendanceSyncCacheDetailComponent } from '../detail/attendance-sync-cache-detail.component';
import { AttendanceSyncCacheUpdateComponent } from '../update/attendance-sync-cache-update.component';
import { AttendanceSyncCacheRoutingResolveService } from './attendance-sync-cache-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const attendanceSyncCacheRoute: Routes = [
  {
    path: '',
    component: AttendanceSyncCacheComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttendanceSyncCacheDetailComponent,
    resolve: {
      attendanceSyncCache: AttendanceSyncCacheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttendanceSyncCacheUpdateComponent,
    resolve: {
      attendanceSyncCache: AttendanceSyncCacheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttendanceSyncCacheUpdateComponent,
    resolve: {
      attendanceSyncCache: AttendanceSyncCacheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attendanceSyncCacheRoute)],
  exports: [RouterModule],
})
export class AttendanceSyncCacheRoutingModule {}
