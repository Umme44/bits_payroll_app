import { Route } from '@angular/router';
import { UpcomingEventContractEndComponent } from './upcoming-event-contract-end.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const UPCOMING_EVENT_CONTRACT_END_ROUTE: Route = {
  path: 'upcoming-event-contract-end',
  component: UpcomingEventContractEndComponent,
  data: {
    authorities: [],
    defaultSort: 'id,asc',
    pageTitle: 'upcoming-event-contract-end.title',
  },
  canActivate: [UserRouteAccessService],
};
