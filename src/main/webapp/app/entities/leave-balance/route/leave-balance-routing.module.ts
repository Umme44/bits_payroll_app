import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveBalanceComponent } from '../list/leave-balance.component';
import { LeaveBalanceDetailComponent } from '../detail/leave-balance-detail.component';
import { LeaveBalanceUpdateComponent } from '../update/leave-balance-update.component';
import { LeaveBalanceRoutingResolveService } from './leave-balance-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const leaveBalanceRoute: Routes = [
  {
    path: '',
    component: LeaveBalanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveBalanceDetailComponent,
    resolve: {
      leaveBalance: LeaveBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveBalanceUpdateComponent,
    resolve: {
      leaveBalance: LeaveBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveBalanceUpdateComponent,
    resolve: {
      leaveBalance: LeaveBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveBalanceRoute)],
  exports: [RouterModule],
})
export class LeaveBalanceRoutingModule {}
