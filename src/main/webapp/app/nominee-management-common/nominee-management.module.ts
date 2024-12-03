import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { nomineeManagementCommonRoutes } from './nominee-management.route';
import { NomineeMasterCommonComponent } from './nominee-master-common/nominee-master-common.component';
import { BitsHrPayrollPfNomineeFormModule } from 'app/provident-fund-management/pf-nominee-form/pf-nominee-form.module';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';
import { NomineeModule } from 'app/entities/nominee/nominee.module';
import { NomineeCrudComponent } from './common-user-crud/nominee-crud.component';
import { PfNomineeFormPrintDetailsComponent } from './report/pf-nominee-report/pf-nominee-form-details-print-format';
import { PfNomineeFormDetailComponent } from './details/pf-nominee/pf-nominee-form-detail.component';
import { NomineeDetailPolishedComponent } from './details/general-and-gf-nominee/nominee-detail-polished.component';
import { GeneralNominationReportComponent } from './report/general-nominee/general-nomination-report.component';
import { GfNomineeReportComponent } from './report/gf-nominee/gf-nominee-report.component';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(nomineeManagementCommonRoutes),
    BitsHrPayrollPfNomineeFormModule,
    BitsHrPayrollHeaderModule,
    NomineeModule,
  ],
  declarations: [
    NomineeMasterCommonComponent,
    NomineeCrudComponent,
    PfNomineeFormPrintDetailsComponent,
    PfNomineeFormDetailComponent,
    NomineeDetailPolishedComponent,
    GeneralNominationReportComponent,
    GfNomineeReportComponent,
  ],
  exports: [NomineeCrudComponent],
})
export class BitsHrPayrollNomineeManagementCommonModule {}
