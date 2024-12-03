import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpecialShiftTimingComponent } from './list/special-shift-timing.component';
import { SpecialShiftTimingDetailComponent } from './detail/special-shift-timing-detail.component';
import { SpecialShiftTimingUpdateComponent } from './update/special-shift-timing-update.component';
import { SpecialShiftTimingDeleteDialogComponent } from './delete/special-shift-timing-delete-dialog.component';
import { SpecialShiftTimingRoutingModule } from './route/special-shift-timing-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {SpecialShiftTimingDetailModalComponent} from "./detail/special-shift-timing-detail-modal.component";

@NgModule({
    imports: [SharedModule, SpecialShiftTimingRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    SpecialShiftTimingComponent,
    SpecialShiftTimingDetailComponent,
    SpecialShiftTimingDetailModalComponent,
    SpecialShiftTimingUpdateComponent,
    SpecialShiftTimingDeleteDialogComponent,
  ],
})
export class SpecialShiftTimingModule {}
