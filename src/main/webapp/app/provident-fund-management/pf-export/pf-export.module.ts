import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { PfExportComponent } from './pf-export.component';
import { pfExportRoute } from './pf-export.route';

@NgModule({
  imports: [SharedModule, BitsHrPayrollHeaderModule, RouterModule.forChild(pfExportRoute)],
  declarations: [PfExportComponent],
})
export class BitsHrPayrollPfExportModule {}
