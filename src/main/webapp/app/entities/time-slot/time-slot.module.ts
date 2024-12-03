import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TimeSlotComponent } from './list/time-slot.component';
import { TimeSlotDetailComponent } from './detail/time-slot-detail.component';
import { TimeSlotUpdateComponent } from './update/time-slot-update.component';
import { TimeSlotDeleteDialogComponent } from './delete/time-slot-delete-dialog.component';
import { TimeSlotRoutingModule } from './route/time-slot-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import { NgSelectModule } from '@ng-select/ng-select';
@NgModule({
    imports: [SharedModule, TimeSlotRoutingModule, BitsHrPayrollHeaderModule,NgSelectModule,],
  declarations: [TimeSlotComponent, TimeSlotDetailComponent, TimeSlotUpdateComponent, TimeSlotDeleteDialogComponent],
})
export class TimeSlotModule {}
