import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HoldFbDisbursementComponent } from './list/hold-fb-disbursement.component';
import { HoldFbDisbursementDetailComponent } from './detail/hold-fb-disbursement-detail.component';
import { HoldFbDisbursementUpdateComponent } from './update/hold-fb-disbursement-update.component';
import { HoldFbDisbursementDeleteDialogComponent } from './delete/hold-fb-disbursement-delete-dialog.component';
import { HoldFbDisbursementRoutingModule } from './route/hold-fb-disbursement-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';
import { HoldFestivalBonusListComponent } from './hold-fb-bonus-list/hold-festival-bonus-list.component';

@NgModule({
  imports: [SharedModule, HoldFbDisbursementRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [
    HoldFbDisbursementComponent,
    HoldFbDisbursementDetailComponent,
    HoldFbDisbursementUpdateComponent,
    HoldFbDisbursementDeleteDialogComponent,
    HoldFestivalBonusListComponent,
  ],
})
export class HoldFbDisbursementModule {}
