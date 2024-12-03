import { Route } from '@angular/router';
import { ExportReportsComponent } from './export-reports.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const EXPORT_REPORTS_ROUTE: Route = {
  path: 'export-reports',
  component: ExportReportsComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.exportReports.home.title',
  },
  canActivate: [UserRouteAccessService],
};
