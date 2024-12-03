import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SalaryCertificateComponent } from '../list/salary-certificate.component';
import { SalaryCertificateDetailComponent } from '../detail/salary-certificate-detail.component';
import { SalaryCertificateUpdateComponent } from '../update/salary-certificate-update.component';
import { SalaryCertificateRoutingResolveService } from './salary-certificate-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from '../../../config/authority.constants';
import { SalaryCertificateApprovalComponent } from '../approval/salary-certificate-approval.component';
import { SalaryCertificatePrintableComponent } from '../approval/salary-certificate-printable.component';

const salaryCertificateRoute: Routes = [
  {
    path: '',
    component: SalaryCertificateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalaryCertificateDetailComponent,
    resolve: {
      salaryCertificate: SalaryCertificateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalaryCertificateUpdateComponent,
    resolve: {
      salaryCertificate: SalaryCertificateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalaryCertificateUpdateComponent,
    resolve: {
      salaryCertificate: SalaryCertificateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/print',
    component: SalaryCertificatePrintableComponent,
    resolve: {
      salaryCertificate: SalaryCertificateRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.salaryCertificate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'approval-hr',
    component: SalaryCertificateApprovalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.salaryCertificate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(salaryCertificateRoute)],
  exports: [RouterModule],
})
export class SalaryCertificateRoutingModule {}
