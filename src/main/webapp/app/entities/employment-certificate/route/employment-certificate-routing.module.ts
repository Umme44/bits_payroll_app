import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmploymentCertificateComponent } from '../list/employment-certificate.component';
import { EmploymentCertificateDetailComponent } from '../detail/employment-certificate-detail.component';
import { EmploymentCertificateUpdateComponent } from '../update/employment-certificate-update.component';
import { EmploymentCertificateRoutingResolveService } from './employment-certificate-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employmentCertificateRoute: Routes = [
  {
    path: '',
    component: EmploymentCertificateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmploymentCertificateDetailComponent,
    resolve: {
      employmentCertificate: EmploymentCertificateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmploymentCertificateUpdateComponent,
    resolve: {
      employmentCertificate: EmploymentCertificateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmploymentCertificateUpdateComponent,
    resolve: {
      employmentCertificate: EmploymentCertificateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employmentCertificateRoute)],
  exports: [RouterModule],
})
export class EmploymentCertificateRoutingModule {}
