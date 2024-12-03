import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { DashboardComponent } from './dashboard.component';
import { DASHBOARD_ROUTE } from './dashboard.route';
import { ApprovalsModalComponent } from './approvals-modal/approvals-modal.component';
import { AttendancesModalComponent } from './attendances-modal/attendances-modal.component';
import { MyStuffsModalComponent } from './my-stuffs-modal/my-stuffs-modal.component';
import { NoticeDetailsModalComponent } from './notice-details-modal/notice-details-modal.component';
import { OfferDetailsModalComponent } from './offer-details-modal/offer-details-modal.component';
import { RequisitionModalComponent } from './requisition-modal/requisition-modal.component';
import { StatementDetailsModalComponent } from './statement-details-modal/statement-details-modal.component';
import { PayslipDetailsModalComponent } from './payslip-details-modal/payslip-details-modal';
import { NomineeDetailsModalComponent } from './nominee-details-modal/nominee-details-modal';
import { SpecialAccessModalComponent } from './special-access-modal/special-access-modal.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([DASHBOARD_ROUTE])],
  declarations: [
    DashboardComponent,
    ApprovalsModalComponent,
    AttendancesModalComponent,
    MyStuffsModalComponent,
    NoticeDetailsModalComponent,
    OfferDetailsModalComponent,
    RequisitionModalComponent,
    StatementDetailsModalComponent,
    PayslipDetailsModalComponent,
    NomineeDetailsModalComponent,
    RequisitionModalComponent,
    SpecialAccessModalComponent,
  ],
})
export class BitsHrPayrollDashboardModule {}
