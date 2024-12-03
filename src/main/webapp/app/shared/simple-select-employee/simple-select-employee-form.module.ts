import { NgModule } from '@angular/core';

import { SharedModule } from '../shared.module';
import { SimpleSelectEmployeeFormComponent } from './simple-select-employee-form.component';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [SimpleSelectEmployeeFormComponent],
  entryComponents: [SimpleSelectEmployeeFormComponent],
  exports: [SimpleSelectEmployeeFormComponent],
})
export class BitsHrPayrollSimpleSelectEmployeeFormModule {}
