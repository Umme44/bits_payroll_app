import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { InsuranceProfileComponent } from './insurance-profile.component';
import { insuranceProfileRoute } from './insurance-profile.route';
import { BitsHrPayrollShowStatusModule } from 'app/shared/show-status/show-stauts.module';
import { UserInsuranceRegistrationDetailsComponent } from './user-insurance-registration-details.component';
import { UserInsuranceRegistrationUpdateComponent } from './user-insurance-registration-update.component';
import { UserInsuranceClaimDetailComponent } from './user-insurance-claim-detail.component';

@NgModule({
  imports: [RouterModule.forChild(insuranceProfileRoute), BitsHrPayrollHeaderModule, BitsHrPayrollShowStatusModule, SharedModule],
  declarations: [
    InsuranceProfileComponent,
    UserInsuranceRegistrationDetailsComponent,
    UserInsuranceClaimDetailComponent,
    UserInsuranceRegistrationUpdateComponent,
  ],
  entryComponents: [InsuranceProfileComponent],
})
export class BitsHrPayrollInsuranceProfileModule {}
