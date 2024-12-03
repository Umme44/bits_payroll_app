import { NgModule } from '@angular/core';

import { SharedModule } from '../shared.module';
import { SelectEmployeeFormComponent } from './select-employee-form.component';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [SelectEmployeeFormComponent],
  entryComponents: [SelectEmployeeFormComponent],
  exports: [SelectEmployeeFormComponent],
})
export class BitsHrPayrollSelectEmployeeFormModule {}
