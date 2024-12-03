import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PfNomineeComponent } from './list/pf-nominee.component';
import { PfNomineeDetailComponent } from './detail/pf-nominee-detail.component';
import { PfNomineeUpdateComponent } from './update/pf-nominee-update.component';
import { PfNomineeDeleteDialogComponent } from './delete/pf-nominee-delete-dialog.component';
import { PfNomineeRoutingModule } from './route/pf-nominee-routing.module';

@NgModule({
  imports: [SharedModule, PfNomineeRoutingModule],
  declarations: [PfNomineeComponent, PfNomineeDetailComponent, PfNomineeUpdateComponent, PfNomineeDeleteDialogComponent],
})
export class PfNomineeModule {}
