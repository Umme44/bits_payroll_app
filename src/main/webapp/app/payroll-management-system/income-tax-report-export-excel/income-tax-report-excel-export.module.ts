import { SharedModule } from '../../shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgModule } from '@angular/core';
import { IncomeTaxReportExcelExportComponent } from './income-tax-report-excel-export.component';
import { RouterModule } from '@angular/router';
import { INCOME_TAX_REPORT_EXCEL_EXPORT } from './income-tax-report-excel-export.route';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([INCOME_TAX_REPORT_EXCEL_EXPORT]), BitsHrPayrollHeaderModule],
  declarations: [IncomeTaxReportExcelExportComponent],
  entryComponents: [],
  providers: [],
})
export class BitsHrPayrollAppIncomeTaxReportExcelExportModule {}
