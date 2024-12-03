import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NomineeComponent } from './list/nominee.component';
import { NomineeDetailComponent } from './detail/nominee-detail.component';
import { NomineeUpdateComponent } from './update/nominee-update.component';
import { NomineeDeleteDialogComponent } from './delete/nominee-delete-dialog.component';
import { NomineeRoutingModule } from './route/nominee-routing.module';

@NgModule({
  imports: [SharedModule, NomineeRoutingModule],
  declarations: [NomineeComponent, NomineeDetailComponent, NomineeUpdateComponent, NomineeDeleteDialogComponent],
})
export class NomineeModule {}
