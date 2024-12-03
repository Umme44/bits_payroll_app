import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FinalSettlementComponent } from '../list/final-settlement.component';
import { FinalSettlementDetailComponent } from '../detail/final-settlement-detail.component';
import { FinalSettlementUpdateComponent } from '../update/final-settlement-update.component';
import { FinalSettlementRoutingResolveService } from './final-settlement-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from '../../../config/authority.constants';
import { PfGfStatementComponent } from '../pf-gf-statement/pf-gf-statement.component';
import { PfStatementComponent } from '../pf-statement/pf-statement.component';
import { EmployeeFinalSettlementComponent } from '../employee-final-settlement/employee-final-settlement.component';

const finalSettlementRoute: Routes = [
  {
    path: '',
    component: FinalSettlementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FinalSettlementDetailComponent,
    resolve: {
      finalSettlement: FinalSettlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':employeeId/pf-gf',
    component: PfGfStatementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':employeeId/pf',
    component: PfStatementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'employee-select',
    component: EmployeeFinalSettlementComponent,
    resolve: {
      finalSettlement: FinalSettlementRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.finalSettlement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FinalSettlementUpdateComponent,
    resolve: {
      finalSettlement: FinalSettlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FinalSettlementUpdateComponent,
    resolve: {
      finalSettlement: FinalSettlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(finalSettlementRoute)],
  exports: [RouterModule],
})
export class FinalSettlementRoutingModule {}
