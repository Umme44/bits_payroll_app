import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OfficeNoticesComponent } from '../list/office-notices.component';
import { OfficeNoticesDetailComponent } from '../detail/office-notices-detail.component';
import { OfficeNoticesUpdateComponent } from '../update/office-notices-update.component';
import { OfficeNoticesRoutingResolveService } from './office-notices-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const officeNoticesRoute: Routes = [
  {
    path: '',
    component: OfficeNoticesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OfficeNoticesDetailComponent,
    resolve: {
      officeNotices: OfficeNoticesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OfficeNoticesUpdateComponent,
    resolve: {
      officeNotices: OfficeNoticesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OfficeNoticesUpdateComponent,
    resolve: {
      officeNotices: OfficeNoticesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(officeNoticesRoute)],
  exports: [RouterModule],
})
export class OfficeNoticesRoutingModule {}
