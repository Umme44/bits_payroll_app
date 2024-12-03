import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaveAllocationComponent } from './list/leave-allocation.component';
import { LeaveAllocationDetailComponent } from './detail/leave-allocation-detail.component';
import { LeaveAllocationUpdateComponent } from './update/leave-allocation-update.component';
import { LeaveAllocationDeleteDialogComponent } from './delete/leave-allocation-delete-dialog.component';
import { LeaveAllocationRoutingModule } from './route/leave-allocation-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";

@NgModule({
    imports: [SharedModule, LeaveAllocationRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    LeaveAllocationComponent,
    LeaveAllocationDetailComponent,
    LeaveAllocationUpdateComponent,
    LeaveAllocationDeleteDialogComponent,
  ],
})
export class LeaveAllocationModule {}
