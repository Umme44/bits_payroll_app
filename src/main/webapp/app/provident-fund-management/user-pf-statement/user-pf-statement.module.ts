import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { USER_PF_STATEMENT_ROUTE } from './user-pf-statement.route';
import { UserPfStatementComponent } from './user-pf-statement.component';

@NgModule({
  imports: [SharedModule, BitsHrPayrollHeaderModule, RouterModule.forChild(USER_PF_STATEMENT_ROUTE)],
  declarations: [UserPfStatementComponent],
})
export class BitsHrPayrollAppUserPfStatementModule {}
