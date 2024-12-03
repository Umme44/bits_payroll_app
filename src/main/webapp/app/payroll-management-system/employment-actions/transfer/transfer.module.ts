import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../../shared/shared.module';
import { TransferComponent } from './transfer.component';
import { TransferListComponent } from './transfer-list.component';
import { TransferDetailComponent } from './transfer-detail.component';
import { TransferDeleteComponent } from './transfer-delete.component';
import { TransferUpdateComponent } from './transfer-update.component';
import { TRANSFER_ROUTE } from './transfer.route';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(TRANSFER_ROUTE), BitsHrPayrollSelectEmployeeFormModule, BitsHrPayrollHeaderModule],
  declarations: [TransferComponent, TransferListComponent, TransferDetailComponent, TransferDeleteComponent, TransferUpdateComponent],
  entryComponents: [TransferDeleteComponent],
})
export class BitsHrPayrollTransferModule {}
