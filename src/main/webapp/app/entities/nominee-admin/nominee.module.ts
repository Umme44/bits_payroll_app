import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../shared/shared.module';
import { nomineeRoute } from './routes/nominee.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { GeneralNomineeDetailsModalComponent } from './modals/general-nominee-details-modal/general-nominee-details.modal.component';
import { PfNomineeDetailsModalComponent } from './modals/pf-nominee-details-modal/pf-nominee-details.modal.component';
import { GeneralNominationReportComponent } from './reports/general-nominee/general-nomination-report.component';
import { GfNomineeReportComponent } from './reports/gf-nominee/gf-nominee-report.component';
import { PfNomineeReportAdminComponent } from './reports/pf-nominee/pf-nominee-report-admin.component';
import { NomineeSummaryAdminComponent } from './summary-view/nominee-summary-admin.component';
import { AdminGeneralNomineeUpdatePolishedComponent } from './update/general-nominee/admin-general-nominee-update-polished.component';
import { AdminGfNomineeUpdatePolishedComponent } from './update/gf-nominee/admin--gf-nominee-update-polished.component';
import { PfNomineeUpdateComponent } from './update/pf-nominee/pf-nominee-update.component';
import { NomineeComponent } from './list/nominee.component';

@NgModule({
  imports: [
    NgSelectModule,
    SharedModule,
    RouterModule.forChild(nomineeRoute),
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSearchTextBoxModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
  ],
  declarations: [
    NomineeComponent,
    GeneralNomineeDetailsModalComponent,
    PfNomineeDetailsModalComponent,
    GeneralNominationReportComponent,
    GfNomineeReportComponent,
    PfNomineeReportAdminComponent,
    NomineeSummaryAdminComponent,
    AdminGeneralNomineeUpdatePolishedComponent,
    AdminGfNomineeUpdatePolishedComponent,
    PfNomineeUpdateComponent,
  ],
  exports: [],
})
export class BitsHrPayrollNomineeModule {}
