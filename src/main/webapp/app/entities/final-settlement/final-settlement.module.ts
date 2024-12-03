import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { FinalSettlementComponent } from './list/final-settlement.component';
import { FinalSettlementDetailComponent } from './detail/final-settlement-detail.component';
import { FinalSettlementUpdateComponent } from './update/final-settlement-update.component';
import { FinalSettlementDeleteDialogComponent } from './delete/final-settlement-delete-dialog.component';
import { FinalSettlementRoutingModule } from './route/final-settlement-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { EmployeeFinalSettlementComponent } from './employee-final-settlement/employee-final-settlement.component';
import { PfStatementComponent } from './pf-statement/pf-statement.component';
import { PfGfStatementComponent } from './pf-gf-statement/pf-gf-statement.component';

@NgModule({
  imports: [SharedModule, FinalSettlementRoutingModule, NgSelectModule, BitsHrPayrollHeaderModule],
  declarations: [
    FinalSettlementComponent,
    FinalSettlementDetailComponent,
    FinalSettlementUpdateComponent,
    FinalSettlementDeleteDialogComponent,
    EmployeeFinalSettlementComponent,
    PfStatementComponent,
    PfGfStatementComponent,
  ],
})
export class FinalSettlementModule {}
