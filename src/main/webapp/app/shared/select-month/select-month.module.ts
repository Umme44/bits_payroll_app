import { NgModule } from '@angular/core';
import { SharedModule } from '../shared.module';
import { SelectMonthComponent } from './select-month.component';

@NgModule({
  imports: [SharedModule],
  declarations: [SelectMonthComponent],
  entryComponents: [SelectMonthComponent],
  exports: [SelectMonthComponent],
})
export class BitsHrPayrollSelectMonthModule {}
