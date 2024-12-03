import { Routes } from '@angular/router';
import {EmployeeIdCardComponent} from "./employee-id-card-details-component";
import {UserRouteAccessService} from "../../core/auth/user-route-access.service";

export const employeeIdCardRoute: Routes = [
  {
    path: '',
    component: EmployeeIdCardComponent,
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: ':id/view',
  //   component: EmployeeStaticFileDetailComponent,
  //   resolve: {
  //     employeeStaticFile: EmployeeStaticFileResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'new',
  //   component: EmployeeStaticFileUpdateComponent,
  //   resolve: {
  //     employeeStaticFile: EmployeeStaticFileResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: ':id/edit',
  //   component: EmployeeStaticFileUpdateComponent,
  //   resolve: {
  //     employeeStaticFile: EmployeeStaticFileResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'id-card-upload',
  //   component: EmployeeIdCardUploadComponent,
  //   resolve: {
  //     employeeStaticFile: EmployeeStaticFileResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'id-card-list',
  //   component: EmployeeIdCardListComponent,
  //   resolve: {
  //     employeeStaticFile: EmployeeStaticFileResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'edit-id-card/:id',
  //   component: EmployeeIdCardUploadUpdateComponent,
  //   resolve: {
  //     employeeStaticFile: EmployeeStaticFileResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'my-id-card',
  //   component: MyIdCardDetailsComponent,
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.id',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
];
