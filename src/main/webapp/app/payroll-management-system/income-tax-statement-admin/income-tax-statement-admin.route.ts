import { Route } from '@angular/router';
import { IncomeTaxStatementAdminComponent } from './income-tax-statement-admin.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const INCOME_TAX_STATEMENT_ADMIN_ROUTE: Route = {
  path: '',
  component: IncomeTaxStatementAdminComponent,
  data: {
    authorities: [],
    pageTitle: 'income-tax-statement.title',
  },
  canActivate: [UserRouteAccessService],
};
