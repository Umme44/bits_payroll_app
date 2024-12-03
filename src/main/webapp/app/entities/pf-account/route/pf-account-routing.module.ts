import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PfAccountComponent } from '../list/pf-account.component';
import { PfAccountDetailComponent } from '../detail/pf-account-detail.component';
import { PfAccountUpdateComponent } from '../update/pf-account-update.component';
import { PfAccountRoutingResolveService } from './pf-account-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import {Authority} from "../../../config/authority.constants";
import {PfStatementComponent} from "../statememt/pf-statement.component";

const pfAccountRoute: Routes = [
  {
    path: '',
    component: PfAccountComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfAccountDetailComponent,
    resolve: {
      pfAccount: PfAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfAccountUpdateComponent,
    resolve: {
      pfAccount: PfAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfAccountUpdateComponent,
    resolve: {
      pfAccount: PfAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/pf-statement',
    component: PfStatementComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.userPfStatement.home',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pfAccountRoute)],
  exports: [RouterModule],
})
export class PfAccountRoutingModule {}
