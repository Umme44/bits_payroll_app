import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { MovementEntryDeleteDialogComponent } from './movement-entry-delete-dialog.component';
import { movementEntryRoute } from './movement-entry.route';
import { UserMovementEntryComponent } from './user-movement-entry.component';
import { MovementEntryApprovalComponent } from './movement-entry-approval.component';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(movementEntryRoute),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [MovementEntryDeleteDialogComponent, UserMovementEntryComponent, MovementEntryApprovalComponent],
  entryComponents: [MovementEntryDeleteDialogComponent],
})
export class BitsHrPayrollMovementEntryModule {}
