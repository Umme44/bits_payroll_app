import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IndividualArrearSalaryComponent } from './list/individual-arrear-salary.component';
import { IndividualArrearSalaryDetailComponent } from './detail/individual-arrear-salary-detail.component';
import { IndividualArrearSalaryUpdateComponent } from './update/individual-arrear-salary-update.component';
import { IndividualArrearSalaryDeleteDialogComponent } from './delete/individual-arrear-salary-delete-dialog.component';
import { IndividualArrearSalaryRoutingModule } from './route/individual-arrear-salary-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { IndividualArrearSameGroupSalariesComponent } from './detailed-list-view/individual-arrear-same-group-salaries.component';

@NgModule({
  imports: [SharedModule, IndividualArrearSalaryRoutingModule, NgSelectModule, BitsHrPayrollHeaderModule],
  declarations: [
    IndividualArrearSalaryComponent,
    IndividualArrearSalaryDetailComponent,
    IndividualArrearSalaryUpdateComponent,
    IndividualArrearSalaryDeleteDialogComponent,
    IndividualArrearSameGroupSalariesComponent,
  ],
})
export class IndividualArrearSalaryModule {}
