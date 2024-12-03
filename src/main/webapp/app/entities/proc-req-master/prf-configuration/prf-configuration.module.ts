import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrfConfigurationUpdateComponent } from './prf-configuration-update.component';
import { prfConfigurationRoute } from './prf-configuration.route';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../../shared/simple-select-employee/simple-select-employee-form.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@NgModule({
  imports: [
    RouterModule.forChild(prfConfigurationRoute),
    BitsHrPayrollHeaderModule,
    NgSelectModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
  ],
  declarations: [PrfConfigurationUpdateComponent],
  entryComponents: [],
})
export class BitsHrPayrollPRFConfigModule {}
