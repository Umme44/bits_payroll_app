import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArrearSalaryComponent } from '../list/arrear-salary.component';
import { ArrearSalaryDetailComponent } from '../detail/arrear-salary-detail.component';
import { ArrearSalaryUpdateComponent } from '../update/arrear-salary-update.component';
import { ArrearSalaryRoutingResolveService } from './arrear-salary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const arrearSalaryRoute: Routes = [
  {
    path: '',
    component: ArrearSalaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArrearSalaryDetailComponent,
    resolve: {
      arrearSalary: ArrearSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArrearSalaryUpdateComponent,
    resolve: {
      arrearSalary: ArrearSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArrearSalaryUpdateComponent,
    resolve: {
      arrearSalary: ArrearSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(arrearSalaryRoute)],
  exports: [RouterModule],
})
export class ArrearSalaryRoutingModule {}
