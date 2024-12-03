import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeStaticFileComponent } from '../list/employee-static-file.component';
import { EmployeeStaticFileDetailComponent } from '../detail/employee-static-file-detail.component';
import { EmployeeStaticFileUpdateComponent } from '../update/employee-static-file-update.component';
import { EmployeeStaticFileRoutingResolveService } from './employee-static-file-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { EmployeeIdCardUploadComponent } from '../employee-id-card-upload/employee-id-card-upload.component';
import { Authority } from '../../../config/authority.constants';
import { EmployeeIdCardListComponent } from '../employee-id-card-upload/employee-id-card-list.component';
import { EmployeeIdCardUploadUpdateComponent } from '../employee-id-card-upload/employee-id-card-upload-update.component';

const employeeStaticFileRoute: Routes = [
  {
    path: '',
    component: EmployeeStaticFileComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeStaticFileDetailComponent,
    resolve: {
      employeeStaticFile: EmployeeStaticFileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeStaticFileUpdateComponent,
    resolve: {
      employeeStaticFile: EmployeeStaticFileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeStaticFileUpdateComponent,
    resolve: {
      employeeStaticFile: EmployeeStaticFileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'id-card-upload',
    component: EmployeeIdCardUploadComponent,
    resolve: {
      employeeStaticFile: EmployeeStaticFileRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'id-card-list',
    component: EmployeeIdCardListComponent,
    resolve: {
      employeeStaticFile: EmployeeStaticFileRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'edit-id-card/:id',
    component: EmployeeIdCardUploadUpdateComponent,
    resolve: {
      employeeStaticFile: EmployeeStaticFileRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeStaticFileRoute)],
  exports: [RouterModule],
})
export class EmployeeStaticFileRoutingModule {}
