import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UserWorkFromHomeApplicationComponent } from './list/user-work-from-home-application.component';
import { UserWorkFromHomeApplicationDetailComponent } from './detail/user-work-from-home-application-detail.component';
import { UserWorkFromHomeApplicationUpdateComponent } from './update/user-work-from-home-application-update.component';
import { UserWorkFromHomeApplicationDeleteDialogComponent } from './delete/user-work-from-home-application-delete-dialog.component';
import { UserWorkFromHomeApplicationRoutingModule } from './route/user-work-from-home-application.routing.module';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../../shared/search-text-box/search-text-box.module';
import { UserWorkFromApplicationDetailModalComponent } from './modal/user-work-from-application-detail-modal.component';
import { BitsHrPayrollWorkFromHomeApplicationLMApprovalsModule } from '../work-from-home-approval/work-from-home-application.module';

@NgModule({
  imports: [
    UserWorkFromHomeApplicationRoutingModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
    SharedModule,
    BitsHrPayrollWorkFromHomeApplicationLMApprovalsModule,
  ],
  declarations: [
    UserWorkFromHomeApplicationComponent,
    UserWorkFromHomeApplicationDetailComponent,
    UserWorkFromHomeApplicationUpdateComponent,
    UserWorkFromHomeApplicationDeleteDialogComponent,
    UserWorkFromApplicationDetailModalComponent,
  ],
})
export class BitsHrPayrollUserWorkFromHomeApplicationModule {}
