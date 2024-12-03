import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VehicleRequisitionComponent } from '../list/vehicle-requisition.component';
import { VehicleRequisitionDetailComponent } from '../detail/vehicle-requisition-detail.component';
import { VehicleRequisitionUpdateComponent } from '../update/vehicle-requisition-update.component';
import { VehicleRequisitionRoutingResolveService } from './vehicle-requisition-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vehicleRequisitionRoute: Routes = [
  {
    path: '',
    component: VehicleRequisitionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VehicleRequisitionDetailComponent,
    resolve: {
      vehicleRequisition: VehicleRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VehicleRequisitionUpdateComponent,
    resolve: {
      vehicleRequisition: VehicleRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VehicleRequisitionUpdateComponent,
    resolve: {
      vehicleRequisition: VehicleRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vehicleRequisitionRoute)],
  exports: [RouterModule],
})
export class VehicleRequisitionRoutingModule {}
