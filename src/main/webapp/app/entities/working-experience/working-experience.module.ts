import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkingExperienceComponent } from './list/working-experience.component';
import { WorkingExperienceDetailComponent } from './detail/working-experience-detail.component';
import { WorkingExperienceUpdateComponent } from './update/working-experience-update.component';
import { WorkingExperienceDeleteDialogComponent } from './delete/working-experience-delete-dialog.component';
import { WorkingExperienceRoutingModule } from './route/working-experience-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';

@NgModule({
  imports: [SharedModule, WorkingExperienceRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSimpleSelectEmployeeFormModule],
  declarations: [
    WorkingExperienceComponent,
    WorkingExperienceDetailComponent,
    WorkingExperienceUpdateComponent,
    WorkingExperienceDeleteDialogComponent,
  ],
})
export class WorkingExperienceModule {}
