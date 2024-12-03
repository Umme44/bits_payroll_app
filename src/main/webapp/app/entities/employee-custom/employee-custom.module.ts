import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NgSelectModule } from '@ng-select/ng-select';
import { EMPLOYEE_CUSTOM_ROUTE, EmployeeCustomComponent } from './index';
import { EmployeeCustomDetailComponent } from './detail/employee-custom-detail.component';
import { EmployeeCustomUpdateComponent } from './update/employee-custom-update.component';
import { EmployeeCustomDeleteDialogComponent } from './delete/employee-custom-delete-dialog.component';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { SharedModule } from '../../shared/shared.module';
import { EducationDetailsModalComponent } from './modals/employee-eduction-details/education-details.modal.component';
import { EducationDetailsUpdateModalComponent } from './modals/employee-eduction-details/education-details-update.modal.component';
import { EmployeeReferencesModalComponent } from './modals/employee-references/employee-references.modal.component';
import { EmployeeReferencesUpdateComponent } from './modals/employee-references/employee-references-update.component';
import { EmployeeTrainingHistoryModalComponent } from './modals/employee-training-history/employee-training-history.modal.component';
import { EmployeeTrainingHistoryUpdateComponent } from './modals/employee-training-history/employee-training-history-update.component';
import { EmployeeWorkExperienceModalComponent } from './modals/employee-work-experiences/employee-work-experience.modal.component';
import { EmployeeWorkExperienceUpdateComponent } from './modals/employee-work-experiences/employee-work-experience-update.component';
import { EmployeeAnalyticsComponent } from './employee-analytics/employee-analytics.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(EMPLOYEE_CUSTOM_ROUTE), BitsHrPayrollHeaderModule, NgSelectModule],
  declarations: [
    EmployeeCustomComponent,
    EmployeeCustomDetailComponent,
    EmployeeCustomUpdateComponent,
    EmployeeCustomDeleteDialogComponent,
    EducationDetailsModalComponent,
    EducationDetailsUpdateModalComponent,
    EmployeeReferencesModalComponent,
    EmployeeReferencesUpdateComponent,
    EmployeeTrainingHistoryModalComponent,
    EmployeeTrainingHistoryUpdateComponent,
    EmployeeWorkExperienceModalComponent,
    EmployeeWorkExperienceUpdateComponent,
    EmployeeAnalyticsComponent,
  ],
  entryComponents: [EmployeeCustomDeleteDialogComponent],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [EmployeeCustomComponent],
})
export class BitsHrPayrollAppEmployeeCustomModule {}
