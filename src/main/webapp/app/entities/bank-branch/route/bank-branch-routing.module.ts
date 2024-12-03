import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BankBranchComponent } from '../list/bank-branch.component';
import { BankBranchDetailComponent } from '../detail/bank-branch-detail.component';
import { BankBranchUpdateComponent } from '../update/bank-branch-update.component';
import { BankBranchRoutingResolveService } from './bank-branch-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bankBranchRoute: Routes = [
  {
    path: '',
    component: BankBranchComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankBranchDetailComponent,
    resolve: {
      bankBranch: BankBranchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BankBranchUpdateComponent,
    resolve: {
      bankBranch: BankBranchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BankBranchUpdateComponent,
    resolve: {
      bankBranch: BankBranchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bankBranchRoute)],
  exports: [RouterModule],
})
export class BankBranchRoutingModule {}
