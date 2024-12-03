import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NationalityComponent } from '../list/nationality.component';
import { NationalityDetailComponent } from '../detail/nationality-detail.component';
import { NationalityUpdateComponent } from '../update/nationality-update.component';
import { NationalityRoutingResolveService } from './nationality-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const nationalityRoute: Routes = [
  {
    path: '',
    component: NationalityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NationalityDetailComponent,
    resolve: {
      nationality: NationalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NationalityUpdateComponent,
    resolve: {
      nationality: NationalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NationalityUpdateComponent,
    resolve: {
      nationality: NationalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nationalityRoute)],
  exports: [RouterModule],
})
export class NationalityRoutingModule {}
