import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NomineeComponent } from '../list/nominee.component';
import { NomineeDetailComponent } from '../detail/nominee-detail.component';
import { NomineeUpdateComponent } from '../update/nominee-update.component';
import { NomineeRoutingResolveService } from './nominee-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const nomineeRoute: Routes = [
  {
    path: '',
    component: NomineeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NomineeDetailComponent,
    resolve: {
      nominee: NomineeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NomineeUpdateComponent,
    resolve: {
      nominee: NomineeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NomineeUpdateComponent,
    resolve: {
      nominee: NomineeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nomineeRoute)],
  exports: [RouterModule],
})
export class NomineeRoutingModule {}
