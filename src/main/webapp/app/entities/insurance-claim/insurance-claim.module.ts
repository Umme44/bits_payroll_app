import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InsuranceClaimComponent } from './list/insurance-claim.component';
import { InsuranceClaimDetailComponent } from './detail/insurance-claim-detail.component';
import { InsuranceClaimUpdateComponent } from './update/insurance-claim-update.component';
import { InsuranceClaimDeleteDialogComponent } from './delete/insurance-claim-delete-dialog.component';
import { InsuranceClaimRoutingModule } from './route/insurance-claim-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";

@NgModule({
    imports: [SharedModule, InsuranceClaimRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    InsuranceClaimComponent,
    InsuranceClaimDetailComponent,
    InsuranceClaimUpdateComponent,
    InsuranceClaimDeleteDialogComponent,
  ],
})
export class InsuranceClaimModule {}
