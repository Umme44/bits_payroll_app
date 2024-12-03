import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EducationDetailsComponent } from './list/education-details.component';
import { EducationDetailsDetailComponent } from './detail/education-details-detail.component';
import { EducationDetailsUpdateComponent } from './update/education-details-update.component';
import { EducationDetailsDeleteDialogComponent } from './delete/education-details-delete-dialog.component';
import { EducationDetailsRoutingModule } from './route/education-details-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';

@NgModule({
  imports: [
    SharedModule,
    EducationDetailsRoutingModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
  ],
  declarations: [
    EducationDetailsComponent,
    EducationDetailsDetailComponent,
    EducationDetailsUpdateComponent,
    EducationDetailsDeleteDialogComponent,
  ],
})
export class EducationDetailsModule {}
