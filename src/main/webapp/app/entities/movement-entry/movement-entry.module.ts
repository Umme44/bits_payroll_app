import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MovementEntryComponent } from './list/movement-entry.component';
import { MovementEntryDetailComponent } from './detail/movement-entry-detail.component';
import { MovementEntryUpdateComponent } from './update/movement-entry-update.component';
import { MovementEntryDeleteDialogComponent } from './delete/movement-entry-delete-dialog.component';
import { MovementEntryRoutingModule } from './route/movement-entry-routing.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { MovementEntryApprovalComponent } from './hr-approval/movement-entry-approval.component';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';
import { BitsHrPayrollShowStatusModule } from '../../shared/show-status/show-stauts.module';

@NgModule({
  imports: [
    SharedModule,
    MovementEntryRoutingModule,
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
    BitsHrPayrollShowStatusModule,
  ],
  declarations: [
    MovementEntryApprovalComponent,
    MovementEntryComponent,
    MovementEntryDetailComponent,
    MovementEntryUpdateComponent,
    MovementEntryDeleteDialogComponent,
  ],
})
export class MovementEntryModule {}
