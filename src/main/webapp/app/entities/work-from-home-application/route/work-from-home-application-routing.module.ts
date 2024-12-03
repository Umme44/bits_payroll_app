import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkFromHomeApplicationComponent } from '../list/work-from-home-application.component';
import { WorkFromHomeApplicationDetailComponent } from '../detail/work-from-home-application-detail.component';
import { WorkFromHomeApplicationUpdateComponent } from '../update/work-from-home-application-update.component';
import { WorkFromHomeApplicationRoutingResolveService } from './work-from-home-application-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const workFromHomeApplicationRoute: Routes = [
  {
    path: '',
    component: WorkFromHomeApplicationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkFromHomeApplicationDetailComponent,
    resolve: {
      workFromHomeApplication: WorkFromHomeApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkFromHomeApplicationUpdateComponent,
    resolve: {
      workFromHomeApplication: WorkFromHomeApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkFromHomeApplicationUpdateComponent,
    resolve: {
      workFromHomeApplication: WorkFromHomeApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workFromHomeApplicationRoute)],
  exports: [RouterModule],
})
export class WorkFromHomeApplicationRoutingModule {}
