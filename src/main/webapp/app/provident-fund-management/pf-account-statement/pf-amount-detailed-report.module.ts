import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { PfAmountDetailedReportComponent } from './pf-amount-detailed-report.component';
import { pfAmountDetailedReportRoute } from './pf-amount-detailed-report.route';

@NgModule({
  imports: [SharedModule, BitsHrPayrollHeaderModule, RouterModule.forChild(pfAmountDetailedReportRoute)],
  declarations: [PfAmountDetailedReportComponent],
})
export class BitsHrPayrollPfAmountDetailedReportModule {}
