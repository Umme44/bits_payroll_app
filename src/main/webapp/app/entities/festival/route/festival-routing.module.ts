import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FestivalComponent } from '../list/festival.component';
import { FestivalDetailComponent } from '../detail/festival-detail.component';
import { FestivalUpdateComponent } from '../update/festival-update.component';
import { FestivalRoutingResolveService } from './festival-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const festivalRoute: Routes = [
  {
    path: '',
    component: FestivalComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FestivalDetailComponent,
    resolve: {
      festival: FestivalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FestivalUpdateComponent,
    resolve: {
      festival: FestivalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FestivalUpdateComponent,
    resolve: {
      festival: FestivalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },


];

@NgModule({
  imports: [RouterModule.forChild(festivalRoute)],
  exports: [RouterModule],
})
export class FestivalRoutingModule {}
