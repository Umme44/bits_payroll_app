import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { SALARY_GENERATION_ROUTE, SalaryGenerationComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([SALARY_GENERATION_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [SalaryGenerationComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppSalaryGenerationModule {}
