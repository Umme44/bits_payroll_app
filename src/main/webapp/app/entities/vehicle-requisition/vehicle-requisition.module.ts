import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VehicleRequisitionComponent } from './list/vehicle-requisition.component';
import { VehicleRequisitionDetailComponent } from './detail/vehicle-requisition-detail.component';
import { VehicleRequisitionUpdateComponent } from './update/vehicle-requisition-update.component';
import { VehicleRequisitionDeleteDialogComponent } from './delete/vehicle-requisition-delete-dialog.component';
import { VehicleRequisitionRoutingModule } from './route/vehicle-requisition-routing.module';

@NgModule({
  imports: [SharedModule, VehicleRequisitionRoutingModule],
  declarations: [
    VehicleRequisitionComponent,
    VehicleRequisitionDetailComponent,
    VehicleRequisitionUpdateComponent,
    VehicleRequisitionDeleteDialogComponent,
  ],
})
export class VehicleRequisitionModule {}
