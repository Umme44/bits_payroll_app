import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MovementEntryComponent } from '../list/movement-entry.component';
import { MovementEntryDetailComponent } from '../detail/movement-entry-detail.component';
import { MovementEntryUpdateComponent } from '../update/movement-entry-update.component';
import { MovementEntryRoutingResolveService } from './movement-entry-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { MovementEntryApprovalComponent } from '../hr-approval/movement-entry-approval.component';

const movementEntryRoute: Routes = [
  {
    path: '',
    component: MovementEntryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MovementEntryDetailComponent,
    resolve: {
      movementEntry: MovementEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MovementEntryUpdateComponent,
    resolve: {
      movementEntry: MovementEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MovementEntryUpdateComponent,
    resolve: {
      movementEntry: MovementEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'approval/hr',
    component: MovementEntryApprovalComponent,
    data: {
      pageTitle: 'bitsHrPayrollApp.movementEntryAdmin.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(movementEntryRoute)],
  exports: [RouterModule],
})
export class MovementEntryRoutingModule {}
