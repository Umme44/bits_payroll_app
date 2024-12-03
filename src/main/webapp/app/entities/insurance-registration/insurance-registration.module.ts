import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InsuranceRegistrationComponent } from './list/insurance-registration.component';
import { InsuranceRegistrationDetailComponent } from './detail/insurance-registration-detail.component';
import { InsuranceRegistrationUpdateComponent } from './update/insurance-registration-update.component';
import { InsuranceRegistrationDeleteDialogComponent } from './delete/insurance-registration-delete-dialog.component';
import { InsuranceRegistrationRoutingModule } from './route/insurance-registration-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';
@NgModule({
  imports: [SharedModule, InsuranceRegistrationRoutingModule, BitsHrPayrollHeaderModule, NgSelectModule],
  declarations: [
    InsuranceRegistrationComponent,
    InsuranceRegistrationDetailComponent,
    InsuranceRegistrationUpdateComponent,
    InsuranceRegistrationDeleteDialogComponent,
  ],
})
export class InsuranceRegistrationModule {}
