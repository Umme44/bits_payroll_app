import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { SingleSalaryGenerationComponent } from './single-salary-generation.component';
import { SINGLE_SALARY_GENERATION_ROUTE } from './single-salary-generation.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([SINGLE_SALARY_GENERATION_ROUTE]),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSelectEmployeeFormModule,
  ],
  declarations: [SingleSalaryGenerationComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppSingleSalaryGenerationModule {}
