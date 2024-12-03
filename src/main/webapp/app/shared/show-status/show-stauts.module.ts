import { NgModule } from '@angular/core';

import { SharedModule } from '../shared.module';
import { ShowStatusComponent } from './show-status.component';

@NgModule({
  imports: [SharedModule],
  declarations: [ShowStatusComponent],
  entryComponents: [ShowStatusComponent],
  exports: [ShowStatusComponent],
})
export class BitsHrPayrollShowStatusModule {}
