import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PfNomineeComponent } from '../list/pf-nominee.component';
import { PfNomineeDetailComponent } from '../detail/pf-nominee-detail.component';
import { PfNomineeUpdateComponent } from '../update/pf-nominee-update.component';
import { PfNomineeRoutingResolveService } from './pf-nominee-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pfNomineeRoute: Routes = [
  {
    path: '',
    component: PfNomineeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfNomineeDetailComponent,
    resolve: {
      pfNominee: PfNomineeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfNomineeUpdateComponent,
    resolve: {
      pfNominee: PfNomineeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfNomineeUpdateComponent,
    resolve: {
      pfNominee: PfNomineeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pfNomineeRoute)],
  exports: [RouterModule],
})
export class PfNomineeRoutingModule {}
