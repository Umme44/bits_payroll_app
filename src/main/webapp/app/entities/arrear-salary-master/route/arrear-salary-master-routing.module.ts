import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArrearSalaryMasterComponent } from '../list/arrear-salary-master.component';
import { ArrearSalaryMasterDetailComponent } from '../detail/arrear-salary-master-detail.component';
import { ArrearSalaryMasterUpdateComponent } from '../update/arrear-salary-master-update.component';
import { ArrearSalaryMasterRoutingResolveService } from './arrear-salary-master-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const arrearSalaryMasterRoute: Routes = [
  {
    path: '',
    component: ArrearSalaryMasterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArrearSalaryMasterDetailComponent,
    resolve: {
      arrearSalaryMaster: ArrearSalaryMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArrearSalaryMasterUpdateComponent,
    resolve: {
      arrearSalaryMaster: ArrearSalaryMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArrearSalaryMasterUpdateComponent,
    resolve: {
      arrearSalaryMaster: ArrearSalaryMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(arrearSalaryMasterRoute)],
  exports: [RouterModule],
})
export class ArrearSalaryMasterRoutingModule {}
