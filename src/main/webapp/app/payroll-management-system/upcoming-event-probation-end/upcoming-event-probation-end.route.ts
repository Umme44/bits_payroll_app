import { Route } from '@angular/router';
import { UpcomingEventProbationEndComponent } from './upcoming-event-probation-end.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const UPCOMING_EVENT_PROBATION_END_ROUTE: Route = {
  path: 'upcoming-event-probation-end',
  component: UpcomingEventProbationEndComponent,
  data: {
    authorities: [],
    defaultSort: 'id,asc',
    pageTitle: 'upcoming-event-probation-end.title',
  },
  canActivate: [UserRouteAccessService],
};
