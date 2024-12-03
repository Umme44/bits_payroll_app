import { SharedModule } from '../shared.module';
import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';

import { SelectPfAccountFormComponent } from './select-pf-account-form.component';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [SelectPfAccountFormComponent],
  entryComponents: [SelectPfAccountFormComponent],
  exports: [SelectPfAccountFormComponent],
})
export class BitsHrPayrollSelectPfAccountFormModule {}
