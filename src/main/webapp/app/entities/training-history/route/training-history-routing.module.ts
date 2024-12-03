import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrainingHistoryComponent } from '../list/training-history.component';
import { TrainingHistoryDetailComponent } from '../detail/training-history-detail.component';
import { TrainingHistoryUpdateComponent } from '../update/training-history-update.component';
import { TrainingHistoryRoutingResolveService } from './training-history-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const trainingHistoryRoute: Routes = [
  {
    path: '',
    component: TrainingHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrainingHistoryDetailComponent,
    resolve: {
      trainingHistory: TrainingHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrainingHistoryUpdateComponent,
    resolve: {
      trainingHistory: TrainingHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrainingHistoryUpdateComponent,
    resolve: {
      trainingHistory: TrainingHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trainingHistoryRoute)],
  exports: [RouterModule],
})
export class TrainingHistoryRoutingModule {}
