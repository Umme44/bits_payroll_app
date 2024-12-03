import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveAllocationComponent } from '../list/leave-allocation.component';
import { LeaveAllocationDetailComponent } from '../detail/leave-allocation-detail.component';
import { LeaveAllocationUpdateComponent } from '../update/leave-allocation-update.component';
import { LeaveAllocationRoutingResolveService } from './leave-allocation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const leaveAllocationRoute: Routes = [
  {
    path: '',
    component: LeaveAllocationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveAllocationDetailComponent,
    resolve: {
      leaveAllocation: LeaveAllocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveAllocationUpdateComponent,
    resolve: {
      leaveAllocation: LeaveAllocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveAllocationUpdateComponent,
    resolve: {
      leaveAllocation: LeaveAllocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveAllocationRoute)],
  exports: [RouterModule],
})
export class LeaveAllocationRoutingModule {}
