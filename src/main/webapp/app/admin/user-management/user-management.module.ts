import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { UserManagementComponent } from './list/user-management.component';
import { UserManagementDetailComponent } from './detail/user-management-detail.component';
import { UserManagementUpdateComponent } from './update/user-management-update.component';
import { UserManagementDeleteDialogComponent } from './delete/user-management-delete-dialog.component';
import { userManagementRoute } from './user-management.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(userManagementRoute), BitsHrPayrollHeaderModule],
  declarations: [
    UserManagementComponent,
    UserManagementDetailComponent,
    UserManagementUpdateComponent,
    UserManagementDeleteDialogComponent,
  ],
})
export class UserManagementModule {}
