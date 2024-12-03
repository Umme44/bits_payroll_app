import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoomTypeComponent } from '../list/room-type.component';
import { RoomTypeDetailComponent } from '../detail/room-type-detail.component';
import { RoomTypeUpdateComponent } from '../update/room-type-update.component';
import { RoomTypeRoutingResolveService } from './room-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const roomTypeRoute: Routes = [
  {
    path: '',
    component: RoomTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomTypeDetailComponent,
    resolve: {
      roomType: RoomTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomTypeUpdateComponent,
    resolve: {
      roomType: RoomTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomTypeUpdateComponent,
    resolve: {
      roomType: RoomTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomTypeRoute)],
  exports: [RouterModule],
})
export class RoomTypeRoutingModule {}
