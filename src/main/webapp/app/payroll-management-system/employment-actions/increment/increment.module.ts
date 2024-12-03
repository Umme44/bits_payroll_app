import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../../shared/shared.module';
import { INCREMENT_ROUTE } from './increment.route';
import { IncrementCreateComponent } from './increment-create.component';
import { IncrementListComponent } from './increment-list.component';
import { IncrementDetailComponent } from './increment-detail.component';
import { IncrementDeleteComponent } from './increment-delete.component';
import { IncrementUpdateComponent } from './increment-update.component';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../../shared/simple-select-employee/simple-select-employee-form.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(INCREMENT_ROUTE),
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
  ],
  declarations: [
    IncrementCreateComponent,
    IncrementListComponent,
    IncrementDetailComponent,
    IncrementDeleteComponent,
    IncrementUpdateComponent,
  ],
  entryComponents: [IncrementDeleteComponent],
})
export class BitsHrPayrollIncrementModule {}
