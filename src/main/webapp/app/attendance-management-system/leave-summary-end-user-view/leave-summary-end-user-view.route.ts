import { Route } from '@angular/router';
import { LeaveSummaryEndUserViewComponent } from './leave-summary-end-user-view.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const LEAVE_SUMMARY_END_USER_VIEW_ROUTE: Route = {
  path: 'leave-summary-end-user-view',
  component: LeaveSummaryEndUserViewComponent,
  data: {
    authorities: [],
    pageTitle: 'leave-summary-end-user-view.title',
  },
  canActivate: [UserRouteAccessService],
};
