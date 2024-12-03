import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArrearSalaryItemComponent } from '../list/arrear-salary-item.component';
import { ArrearSalaryItemDetailComponent } from '../detail/arrear-salary-item-detail.component';
import { ArrearSalaryItemUpdateComponent } from '../update/arrear-salary-item-update.component';
import { ArrearSalaryItemRoutingResolveService } from './arrear-salary-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const arrearSalaryItemRoute: Routes = [
  {
    path: '',
    component: ArrearSalaryItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArrearSalaryItemDetailComponent,
    resolve: {
      arrearSalaryItem: ArrearSalaryItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArrearSalaryItemUpdateComponent,
    resolve: {
      arrearSalaryItem: ArrearSalaryItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArrearSalaryItemUpdateComponent,
    resolve: {
      arrearSalaryItem: ArrearSalaryItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(arrearSalaryItemRoute)],
  exports: [RouterModule],
})
export class ArrearSalaryItemRoutingModule {}
