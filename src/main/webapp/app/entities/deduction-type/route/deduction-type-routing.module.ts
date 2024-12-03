import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeductionTypeComponent } from '../list/deduction-type.component';
import { DeductionTypeDetailComponent } from '../detail/deduction-type-detail.component';
import { DeductionTypeUpdateComponent } from '../update/deduction-type-update.component';
import { DeductionTypeRoutingResolveService } from './deduction-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const deductionTypeRoute: Routes = [
  {
    path: '',
    component: DeductionTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeductionTypeDetailComponent,
    resolve: {
      deductionType: DeductionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeductionTypeUpdateComponent,
    resolve: {
      deductionType: DeductionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeductionTypeUpdateComponent,
    resolve: {
      deductionType: DeductionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deductionTypeRoute)],
  exports: [RouterModule],
})
export class DeductionTypeRoutingModule {}
