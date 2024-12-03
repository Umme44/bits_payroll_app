import { Routes } from '@angular/router';
import { UserPfStatementComponent } from './user-pf-statement.component';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const USER_PF_STATEMENT_ROUTE: Routes = [
  {
    path: '',
    component: UserPfStatementComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.userPfStatement.home',
    },
    canActivate: [UserRouteAccessService],
  },
];
