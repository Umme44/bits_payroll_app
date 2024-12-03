import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProcReqMasterComponent } from '../list/proc-req-master.component';
import { ProcReqMasterDetailComponent } from '../detail/proc-req-master-detail.component';
import { ProcReqMasterUpdateComponent } from '../update/proc-req-master-update.component';
import { ProcReqMasterRoutingResolveService } from './proc-req-master-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { ProcReqPrintFormComponent } from '../print-form/proc-req-print-form.component';

const procReqMasterRoute: Routes = [
  {
    path: '',
    component: ProcReqMasterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcReqMasterDetailComponent,
    resolve: {
      procReqMaster: ProcReqMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcReqMasterUpdateComponent,
    resolve: {
      procReqMaster: ProcReqMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcReqMasterUpdateComponent,
    resolve: {
      procReqMaster: ProcReqMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/print-view',
    component: ProcReqPrintFormComponent,
    resolve: {
      procReqMaster: ProcReqMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'configuration',
    loadChildren: () => import('../prf-configuration/prf-configuration.module').then(m => m.BitsHrPayrollPRFConfigModule),
  },
];

@NgModule({
  imports: [RouterModule.forChild(procReqMasterRoute)],
  exports: [RouterModule],
})
export class ProcReqMasterRoutingModule {}
