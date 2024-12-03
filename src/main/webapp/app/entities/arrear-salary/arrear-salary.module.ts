import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArrearSalaryComponent } from './list/arrear-salary.component';
import { ArrearSalaryDetailComponent } from './detail/arrear-salary-detail.component';
import { ArrearSalaryUpdateComponent } from './update/arrear-salary-update.component';
import { ArrearSalaryDeleteDialogComponent } from './delete/arrear-salary-delete-dialog.component';
import { ArrearSalaryRoutingModule } from './route/arrear-salary-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";

@NgModule({
    imports: [SharedModule, ArrearSalaryRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [ArrearSalaryComponent, ArrearSalaryDetailComponent, ArrearSalaryUpdateComponent, ArrearSalaryDeleteDialogComponent],
})
export class ArrearSalaryModule {}
