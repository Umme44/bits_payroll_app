import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemInformationComponent } from '../list/item-information.component';
import { ItemInformationDetailComponent } from '../detail/item-information-detail.component';
import { ItemInformationUpdateComponent } from '../update/item-information-update.component';
import { ItemInformationRoutingResolveService } from './item-information-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const itemInformationRoute: Routes = [
  {
    path: '',
    component: ItemInformationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemInformationDetailComponent,
    resolve: {
      itemInformation: ItemInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemInformationUpdateComponent,
    resolve: {
      itemInformation: ItemInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemInformationUpdateComponent,
    resolve: {
      itemInformation: ItemInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemInformationRoute)],
  exports: [RouterModule],
})
export class ItemInformationRoutingModule {}
