import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ASC} from "../../../config/navigation.constants";
import {UserRouteAccessService} from "../../../core/auth/user-route-access.service";
import {LeavePolicyManagementComponent} from "../leave-policy-management.component";
import { FormsComponent } from '../forms/forms.component';

const routes: Routes = [

  {
    path: '',
    component: LeavePolicyManagementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },


  },
  {
    path: 'LeavePolicyManagementComponent/',
    component: FormsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },


  }


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RouteRoutingModule { }
