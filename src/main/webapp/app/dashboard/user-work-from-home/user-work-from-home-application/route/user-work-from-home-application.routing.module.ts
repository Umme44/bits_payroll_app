import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserWorkFromHomeApplicationComponent } from '../list/user-work-from-home-application.component';
import { UserWorkFromHomeApplicationDetailComponent } from '../detail/user-work-from-home-application-detail.component';
import { UserWorkFromHomeApplicationUpdateComponent } from '../update/user-work-from-home-application-update.component';
import { Authority } from '../../../../config/authority.constants';
import { UserRouteAccessService } from '../../../../core/auth/user-route-access.service';
import { WorkFromHomeApplicationResolve } from './user-work-from-home-application-routing-resolve.service';

export const userWorkFromHomeApplicationRoutingModule: Routes = [
  {
    path: '',
    component: UserWorkFromHomeApplicationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.workFromHomeApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserWorkFromHomeApplicationDetailComponent,
    resolve: {
      workFromHomeApplication: WorkFromHomeApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.workFromHomeApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserWorkFromHomeApplicationUpdateComponent,
    resolve: {
      workFromHomeApplication: WorkFromHomeApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.workFromHomeApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserWorkFromHomeApplicationUpdateComponent,
    resolve: {
      workFromHomeApplication: WorkFromHomeApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.workFromHomeApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userWorkFromHomeApplicationRoutingModule)],
  exports: [RouterModule],
})
export class UserWorkFromHomeApplicationRoutingModule {}
