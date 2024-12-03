import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoomRequisitionComponent } from './list/room-requisition.component';
import { RoomRequisitionDetailComponent } from './detail/room-requisition-detail.component';
import { RoomRequisitionUpdateComponent } from './update/room-requisition-update.component';
import { RoomRequisitionDeleteDialogComponent } from './delete/room-requisition-delete-dialog.component';
import { RoomRequisitionRoutingModule } from './route/room-requisition-routing.module';

@NgModule({
  imports: [SharedModule, RoomRequisitionRoutingModule],
  declarations: [
    RoomRequisitionComponent,
    RoomRequisitionDetailComponent,
    RoomRequisitionUpdateComponent,
    RoomRequisitionDeleteDialogComponent,
  ],
})
export class RoomRequisitionModule {}
