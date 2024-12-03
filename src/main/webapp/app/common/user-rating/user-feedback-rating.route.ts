import { Route } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { UserFeedbackRatingComponent } from './user-feedback-rating.component';
import { Authority } from '../../config/authority.constants';

export const USER_FEEDBACK_RATING_ROUTE: Route = {
  path: 'user-feedback/rating',
  component: UserFeedbackRatingComponent,
  data: {
    authorities: [Authority.USER],
    pageTitle: 'bitsHrPayrollApp.userFeedback.home.title',
  },
  canActivate: [UserRouteAccessService],
};
