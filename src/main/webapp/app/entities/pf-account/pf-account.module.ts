import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PfAccountComponent } from './list/pf-account.component';
import { PfAccountDetailComponent } from './detail/pf-account-detail.component';
import { PfAccountUpdateComponent } from './update/pf-account-update.component';
import { PfAccountDeleteDialogComponent } from './delete/pf-account-delete-dialog.component';
import { PfAccountRoutingModule } from './route/pf-account-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';
import {PfStatementComponent} from "./statememt/pf-statement.component";

@NgModule({
  imports: [SharedModule, PfAccountRoutingModule, BitsHrPayrollHeaderModule, NgSelectModule],
  declarations: [PfAccountComponent, PfAccountDetailComponent, PfAccountUpdateComponent, PfAccountDeleteDialogComponent,PfStatementComponent],
})
export class PfAccountModule {}
