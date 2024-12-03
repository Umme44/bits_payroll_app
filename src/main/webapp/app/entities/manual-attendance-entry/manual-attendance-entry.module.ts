import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ManualAttendanceEntryComponent } from './list/manual-attendance-entry.component';
import { ManualAttendanceEntryDetailComponent } from './detail/manual-attendance-entry-detail.component';
import { ManualAttendanceEntryUpdateComponent } from './update/manual-attendance-entry-update.component';
import { ManualAttendanceEntryDeleteDialogComponent } from './delete/manual-attendance-entry-delete-dialog.component';
import { ManualAttendanceEntryRoutingModule } from './route/manual-attendance-entry-routing.module';

@NgModule({
  imports: [SharedModule, ManualAttendanceEntryRoutingModule],
  declarations: [
    ManualAttendanceEntryComponent,
    ManualAttendanceEntryDetailComponent,
    ManualAttendanceEntryUpdateComponent,
    ManualAttendanceEntryDeleteDialogComponent,
  ],
})
export class ManualAttendanceEntryModule {}
