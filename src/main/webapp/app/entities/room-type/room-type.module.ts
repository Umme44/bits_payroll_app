import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoomTypeComponent } from './list/room-type.component';
import { RoomTypeDetailComponent } from './detail/room-type-detail.component';
import { RoomTypeUpdateComponent } from './update/room-type-update.component';
import { RoomTypeDeleteDialogComponent } from './delete/room-type-delete-dialog.component';
import { RoomTypeRoutingModule } from './route/room-type-routing.module';

@NgModule({
  imports: [SharedModule, RoomTypeRoutingModule],
  declarations: [RoomTypeComponent, RoomTypeDetailComponent, RoomTypeUpdateComponent, RoomTypeDeleteDialogComponent],
})
export class RoomTypeModule {}
