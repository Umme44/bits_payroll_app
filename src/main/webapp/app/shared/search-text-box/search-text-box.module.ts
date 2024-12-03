import { NgModule } from '@angular/core';
import { SharedModule } from '../shared.module';
import { SearchTextBoxComponent } from './search-text-box.component';

@NgModule({
  imports: [SharedModule],
  declarations: [SearchTextBoxComponent],
  entryComponents: [SearchTextBoxComponent],
  exports: [SearchTextBoxComponent],
})
export class BitsHrPayrollSearchTextBoxModule {}
