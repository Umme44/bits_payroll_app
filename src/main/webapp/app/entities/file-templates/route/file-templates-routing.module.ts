import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileTemplatesComponent } from '../list/file-templates.component';
import { FileTemplatesDetailComponent } from '../detail/file-templates-detail.component';
import { FileTemplatesUpdateComponent } from '../update/file-templates-update.component';
import { FileTemplatesRoutingResolveService } from './file-templates-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fileTemplatesRoute: Routes = [
  {
    path: '',
    component: FileTemplatesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileTemplatesDetailComponent,
    resolve: {
      fileTemplates: FileTemplatesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileTemplatesUpdateComponent,
    resolve: {
      fileTemplates: FileTemplatesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileTemplatesUpdateComponent,
    resolve: {
      fileTemplates: FileTemplatesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileTemplatesRoute)],
  exports: [RouterModule],
})
export class FileTemplatesRoutingModule {}
