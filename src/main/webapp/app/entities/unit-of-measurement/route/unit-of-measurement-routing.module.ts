import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UnitOfMeasurementComponent } from '../list/unit-of-measurement.component';
import { UnitOfMeasurementDetailComponent } from '../detail/unit-of-measurement-detail.component';
import { UnitOfMeasurementUpdateComponent } from '../update/unit-of-measurement-update.component';
import { UnitOfMeasurementRoutingResolveService } from './unit-of-measurement-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const unitOfMeasurementRoute: Routes = [
  {
    path: '',
    component: UnitOfMeasurementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UnitOfMeasurementDetailComponent,
    resolve: {
      unitOfMeasurement: UnitOfMeasurementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UnitOfMeasurementUpdateComponent,
    resolve: {
      unitOfMeasurement: UnitOfMeasurementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UnitOfMeasurementUpdateComponent,
    resolve: {
      unitOfMeasurement: UnitOfMeasurementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(unitOfMeasurementRoute)],
  exports: [RouterModule],
})
export class UnitOfMeasurementRoutingModule {}
