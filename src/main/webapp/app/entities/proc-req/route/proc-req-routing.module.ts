import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProcReqComponent } from '../list/proc-req.component';
import { ProcReqDetailComponent } from '../detail/proc-req-detail.component';
import { ProcReqUpdateComponent } from '../update/proc-req-update.component';
import { ProcReqRoutingResolveService } from './proc-req-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { ProcReqPrintFormComponent } from '../print-form/proc-req-print-form.component';
import { ProcurementRequisitionApprovalComponent } from '../approval/procurement-requisition-approval.component';

const procReqRoute: Routes = [
  {
    path: '',
    component: ProcReqComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcReqDetailComponent,
    resolve: {
      procReqMaster: ProcReqRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcReqUpdateComponent,
    resolve: {
      procReqMaster: ProcReqRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcReqUpdateComponent,
    resolve: {
      procReqMaster: ProcReqRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/print-view',
    component: ProcReqPrintFormComponent,
    resolve: {
      procReqMaster: ProcReqRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'approval',
    component: ProcurementRequisitionApprovalComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(procReqRoute)],
  exports: [RouterModule],
})
export class ProcReqRoutingModule {}
