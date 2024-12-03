import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PfImportComponent } from './pf-import.component';
import { pfImportRoute } from './pf-import.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, BitsHrPayrollHeaderModule, RouterModule.forChild(pfImportRoute)],
  declarations: [PfImportComponent],
})
export class BitsHrPayrollPfImportModule {}
