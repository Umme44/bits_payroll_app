import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { EXPORT_REPORTS_ROUTE, ExportReportsComponent } from './index';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([EXPORT_REPORTS_ROUTE], { useHash: true })],
  declarations: [ExportReportsComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppExportReportsModule {}
