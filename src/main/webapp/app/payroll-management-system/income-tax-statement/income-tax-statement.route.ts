import { Route } from '@angular/router';
import { IncomeTaxStatementComponent } from './income-tax-statement.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const INCOME_TAX_STATEMENT_ROUTE: Route = {
  path: '',
  component: IncomeTaxStatementComponent,
  data: {
    authorities: [],
    pageTitle: 'income-tax-statement.title',
  },
  canActivate: [UserRouteAccessService],
};
