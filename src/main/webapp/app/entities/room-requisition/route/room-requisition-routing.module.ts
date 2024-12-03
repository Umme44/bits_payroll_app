import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoomRequisitionComponent } from '../list/room-requisition.component';
import { RoomRequisitionDetailComponent } from '../detail/room-requisition-detail.component';
import { RoomRequisitionUpdateComponent } from '../update/room-requisition-update.component';
import { RoomRequisitionRoutingResolveService } from './room-requisition-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const roomRequisitionRoute: Routes = [
  {
    path: '',
    component: RoomRequisitionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomRequisitionDetailComponent,
    resolve: {
      roomRequisition: RoomRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomRequisitionUpdateComponent,
    resolve: {
      roomRequisition: RoomRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomRequisitionUpdateComponent,
    resolve: {
      roomRequisition: RoomRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomRequisitionRoute)],
  exports: [RouterModule],
})
export class RoomRequisitionRoutingModule {}
