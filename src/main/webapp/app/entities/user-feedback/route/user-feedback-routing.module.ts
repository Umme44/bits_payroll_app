import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserFeedbackComponent } from '../list/user-feedback.component';
import { UserFeedbackDetailComponent } from '../detail/user-feedback-detail.component';
import { UserFeedbackUpdateComponent } from '../update/user-feedback-update.component';
import { UserFeedbackRoutingResolveService } from './user-feedback-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const userFeedbackRoute: Routes = [
  {
    path: '',
    component: UserFeedbackComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserFeedbackDetailComponent,
    resolve: {
      userFeedback: UserFeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserFeedbackUpdateComponent,
    resolve: {
      userFeedback: UserFeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserFeedbackUpdateComponent,
    resolve: {
      userFeedback: UserFeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userFeedbackRoute)],
  exports: [RouterModule],
})
export class UserFeedbackRoutingModule {}
