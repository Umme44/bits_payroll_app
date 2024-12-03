import { Route } from '@angular/router';
import { IncomeTaxReportExcelExportComponent } from './income-tax-report-excel-export.component';

export const INCOME_TAX_REPORT_EXCEL_EXPORT: Route = {
  path: 'export-income-tax-report',
  component: IncomeTaxReportExcelExportComponent,
  data: {
    authorities: [],
    pageTitle: 'income-tax-statement.title',
  },
};
