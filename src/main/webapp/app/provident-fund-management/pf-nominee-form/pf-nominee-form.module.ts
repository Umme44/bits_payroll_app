import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PfNomineeFormComponent } from './unused-code/pf-nominee-form.component';
import { pfNomineeFormRoute } from './pf-nominee-form.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { PfNomineeFormDetailComponent } from './pf-nominee-form-detail.component';
import { PfNomineeFormCrudComponent } from './common-user-crud/pf-nominee-form-crud.component';
import { PfNomineeFormPrintDetailsComponent } from './print-format/pf-nominee-form-details-print-format';

@NgModule({
  imports: [SharedModule, BitsHrPayrollHeaderModule, RouterModule.forChild(pfNomineeFormRoute)],
  declarations: [PfNomineeFormComponent, PfNomineeFormDetailComponent, PfNomineeFormCrudComponent, PfNomineeFormPrintDetailsComponent],
  entryComponents: [],
  exports: [PfNomineeFormCrudComponent],
})
export class BitsHrPayrollPfNomineeFormModule {}
