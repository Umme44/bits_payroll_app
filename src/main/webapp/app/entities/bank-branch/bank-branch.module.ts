import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BankBranchComponent } from './list/bank-branch.component';
import { BankBranchDetailComponent } from './detail/bank-branch-detail.component';
import { BankBranchUpdateComponent } from './update/bank-branch-update.component';
import { BankBranchDeleteDialogComponent } from './delete/bank-branch-delete-dialog.component';
import { BankBranchRoutingModule } from './route/bank-branch-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, BankBranchRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [BankBranchComponent, BankBranchDetailComponent, BankBranchUpdateComponent, BankBranchDeleteDialogComponent],
})
export class BankBranchModule {}
