import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArrearSalaryMasterComponent } from './list/arrear-salary-master.component';
import { ArrearSalaryMasterDetailComponent } from './detail/arrear-salary-master-detail.component';
import { ArrearSalaryMasterUpdateComponent } from './update/arrear-salary-master-update.component';
import { ArrearSalaryMasterDeleteDialogComponent } from './delete/arrear-salary-master-delete-dialog.component';
import { ArrearSalaryMasterRoutingModule } from './route/arrear-salary-master-routing.module';

@NgModule({
  imports: [SharedModule, ArrearSalaryMasterRoutingModule],
  declarations: [
    ArrearSalaryMasterComponent,
    ArrearSalaryMasterDetailComponent,
    ArrearSalaryMasterUpdateComponent,
    ArrearSalaryMasterDeleteDialogComponent,
  ],
})
export class ArrearSalaryMasterModule {}
