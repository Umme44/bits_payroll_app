import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { ReportOldComponent } from './report-old/report-old.component';
import { ReportNewComponent } from './report-new/report-new.component';

@NgModule({
  imports: [BitsHrPayrollHeaderModule, SharedModule],
  declarations: [ReportOldComponent, ReportNewComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [ReportNewComponent, ReportOldComponent],
})
export class BitsHrPayrollAppIncomeTaxReportsModule {}
