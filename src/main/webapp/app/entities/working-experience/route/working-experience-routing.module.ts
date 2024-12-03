import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkingExperienceComponent } from '../list/working-experience.component';
import { WorkingExperienceDetailComponent } from '../detail/working-experience-detail.component';
import { WorkingExperienceUpdateComponent } from '../update/working-experience-update.component';
import { WorkingExperienceRoutingResolveService } from './working-experience-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const workingExperienceRoute: Routes = [
  {
    path: '',
    component: WorkingExperienceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkingExperienceDetailComponent,
    resolve: {
      workingExperience: WorkingExperienceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkingExperienceUpdateComponent,
    resolve: {
      workingExperience: WorkingExperienceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkingExperienceUpdateComponent,
    resolve: {
      workingExperience: WorkingExperienceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workingExperienceRoute)],
  exports: [RouterModule],
})
export class WorkingExperienceRoutingModule {}
