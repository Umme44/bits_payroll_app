import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IncomeTaxChallanComponent } from './list/income-tax-challan.component';
import { IncomeTaxChallanDetailComponent } from './detail/income-tax-challan-detail.component';
import { IncomeTaxChallanUpdateComponent } from './update/income-tax-challan-update.component';
import { IncomeTaxChallanDeleteDialogComponent } from './delete/income-tax-challan-delete-dialog.component';
import { IncomeTaxChallanRoutingModule } from './route/income-tax-challan-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, IncomeTaxChallanRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    IncomeTaxChallanComponent,
    IncomeTaxChallanDetailComponent,
    IncomeTaxChallanUpdateComponent,
    IncomeTaxChallanDeleteDialogComponent,
  ],
})
export class IncomeTaxChallanModule {}
