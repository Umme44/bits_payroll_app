import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../../shared/shared.module';
import { BATCH_OPERATIONS_ROUTE } from './batch-operations.route';
import { BatchOperationsComponent } from './batch-operations.component';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(BATCH_OPERATIONS_ROUTE), BitsHrPayrollHeaderModule],
  declarations: [BatchOperationsComponent],
  entryComponents: [BatchOperationsComponent],
})
export class BitsHrPayrollBatchOperationsModule {}
