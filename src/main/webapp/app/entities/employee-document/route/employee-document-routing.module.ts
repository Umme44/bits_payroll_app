import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeDocumentComponent } from '../list/employee-document.component';
import { EmployeeDocumentDetailComponent } from '../detail/employee-document-detail.component';
import { EmployeeDocumentUpdateComponent } from '../update/employee-document-update.component';
import { EmployeeDocumentRoutingResolveService } from './employee-document-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeeDocumentRoute: Routes = [
  {
    path: '',
    component: EmployeeDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeDocumentDetailComponent,
    resolve: {
      employeeDocument: EmployeeDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeDocumentUpdateComponent,
    resolve: {
      employeeDocument: EmployeeDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeDocumentUpdateComponent,
    resolve: {
      employeeDocument: EmployeeDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeDocumentRoute)],
  exports: [RouterModule],
})
export class EmployeeDocumentRoutingModule {}
