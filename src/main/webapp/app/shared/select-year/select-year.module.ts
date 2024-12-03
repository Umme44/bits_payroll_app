import { NgModule } from '@angular/core';
import { SharedModule } from '../shared.module';
import { SelectYearComponent } from './select-year.component';

@NgModule({
  imports: [SharedModule],
  declarations: [SelectYearComponent],
  entryComponents: [SelectYearComponent],
  exports: [SelectYearComponent],
})
export class BitsHrPayrollSelectYearModule {}
