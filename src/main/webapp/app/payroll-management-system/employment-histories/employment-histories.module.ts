import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { EmploymentHistoriesComponent, employmentHistoriesRoutes } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';
@NgModule({
  imports: [SharedModule, NgSelectModule, RouterModule.forChild(employmentHistoriesRoutes), BitsHrPayrollHeaderModule],
  declarations: [EmploymentHistoriesComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppEmploymentHistoriesModule {}
