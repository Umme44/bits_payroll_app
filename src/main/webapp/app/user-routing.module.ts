import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Authority } from './config/authority.constants';
import { UserRouteAccessService } from './core/auth/user-route-access.service';
import { EmployeeAddressBookComponent } from './common/employee-address-book/employee-address-book.component';
import { BloodBankComponent } from './common/blood-bank/blood-bank.component';
import { MyTeamComponent } from './attendance-management-system/my-team/my-team.component';
import { AttendanceTimeSheetMyTeamComponent } from './attendance-management-system/ats/attendance-time-sheet-admin/attendance-time-sheet-my-team.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dashboard',
        data: {
          authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.BitsHrPayrollDashboardModule),
      },
      {
        path: 'employee-address-book',
        canActivate: [UserRouteAccessService],
        component: EmployeeAddressBookComponent,
        data: {
          authorities: [Authority.USER],
          defaultSort: 'pin,asc',
          pageTitle: 'bitsHrPayrollApp.addressBook.home',
        },
      },
      {
        path: 'blood-bank',
        canActivate: [UserRouteAccessService],
        component: BloodBankComponent,
        data: {
          authorities: [Authority.USER],
          defaultSort: 'id,asc',
          pageTitle: 'bitsHrPayrollApp.bloodBank.home.title',
        },
      },
      {
        path: 'holiday-calendar',
        canActivate: [UserRouteAccessService],
        data: {
          authorities: [Authority.USER],
          defaultSort: 'id,asc',
          pageTitle: 'bitsHrPayrollApp.bloodBank.home.title',
        },
        loadChildren: () => import('./common/holiday-calendar/holiday-calendar.module').then(m => m.BitsHrPayrollAppHolidayCalendarModule),
      },
      {
        path: '',
        data: {
          authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () =>
          import('./payroll-management-system/user-leave-application').then(m => m.BitsHrPayrollAppUserLeaveApplicationModule),
      },
      {
        path: '',
        data: {
          authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () =>
          import('./attendance-management-system/ats/attendance-time-sheet/attendance-time-sheet.module').then(
            m => m.BitsHrPayrollAppAttendanceTimeSheetModule
          ),
      },
      {
        path: '',
        data: {
          authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () =>
          import('./payroll-management-system/user-payslip/user-payslip.module').then(m => m.BitsHrPayrollAppUserPayslipModule),
      },
      {
        path: '',
        data: {
          authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () =>
          import('./attendance-management-system/leave-approval-superordinate/leave-approval-superordinate.module').then(
            m => m.BitsHrPayrollAppLeaveApprovalSuperordinateModule
          ),
      },
      {
        path: 'manual-attendance-entry',
        data: { pageTitle: 'bitsHrPayrollApp.manualAttendanceEntry.home.title' },
        loadChildren: () =>
          import('./attendance-management-system/user-manual-attendance-entry/manual-attendance-entry.module').then(
            m => m.BitsHrPayrollManualAttendanceEntryUserModule
          ),
      },
      {
        path: 'my-team',
        loadChildren: () => import('./attendance-management-system/my-team/my-team.module').then(m => m.BitsHrPayrollMyTeamModule),
      },
      // my-id-card
      {
        path: 'my-id-card',
        data: {
          pageTitle: 'myIdCardPage.home.title',
        },
        loadChildren: () =>
          import('./payroll-management-system/employee-id-card/employee-id-card.module').then(m => m.BitsHrPayrollEmployeeIdCardModule),
      },
      {
        path: 'work-from-home-application/approvals',
        loadChildren: () =>
          import('./dashboard/user-work-from-home/work-from-home-approval/work-from-home-application.module').then(
            m => m.BitsHrPayrollWorkFromHomeApplicationLMApprovalsModule
          ),
      },
      {
        path: 'user-work-from-home-application',
        loadChildren: () =>
          import('./dashboard/user-work-from-home/user-work-from-home-application/user-work-from-home-application.module').then(
            m => m.BitsHrPayrollUserWorkFromHomeApplicationModule
          ),
      },
      {
        path: 'user-leave-application-status-and-history',
        loadChildren: () =>
          import(
            './payroll-management-system/user-leave-application-status-and-history/user-leave-application-status-and-history.module'
          ).then(m => m.BitsHrPayrollAppUserLeaveApplicationStatusAndHistoryModule),
      },
      {
        path: 'my-provident-fund-statement',
        loadChildren: () =>
          import('./provident-fund-management/user-pf-statement/user-pf-statement.module').then(
            m => m.BitsHrPayrollAppUserPfStatementModule
          ),
      },
      {
        path: 'income-tax-statement',
        loadChildren: () =>
          import('./payroll-management-system/income-tax-statement/income-tax-statement.module').then(
            m => m.BitsHrPayrollAppIncomeTaxStatementModule
          ),
      },
      {
        path: 'employee-docs',
        loadChildren: () =>
          import('./payroll-management-system/employment docs/employee-docs.module').then(m => m.BitsHrPayrollEmployeeDOCsModule),
      },

      {
        path: 'movement-entry',
        data: { pageTitle: 'bitsHrPayrollApp.movementEntry.home.title' },
        loadChildren: () =>
          import('./payroll-management-system/movement-entry/movement-entry.module').then(m => m.BitsHrPayrollMovementEntryModule),
      },

      {
        path: 'user-flex-schedule-application',
        loadChildren: () =>
          import(
            './payroll-management-system/user-flex-schedule/flex-schedule-application-user/flex-schedule-application-user.module'
          ).then(m => m.BitsHrPayrollFlexScheduleApplicationUserModule),
      },

      {
        path: 'flex-schedule-application-approval',
        loadChildren: () =>
          import(
            './payroll-management-system/user-flex-schedule/flex-schedule-application-approval/flex-schedule-application-approval.module'
          ).then(m => m.BitsHrPayrollAppFlexScheduleApplicationApprovalModule),
      },

      {
        path: 'recruitment-requisition-form',
        loadChildren: () =>
          import('./payroll-management-system/recruitment-requisition-form/recruitment-requisition-form.module').then(
            m => m.BitsHrPayrollRecruitmentRequisitionFormModule
          ),
      },

      {
        path: 'file-templates',
        loadChildren: () =>
          import('./payroll-management-system/file-templates/file-templates.module').then(m => m.BitsHrPayrollFileTemplatesModule),
      },

      {
        path: 'insurance-profile',
        loadChildren: () =>
          import('./payroll-management-system/insurance-profile/insurance-profile.module').then(m => m.BitsHrPayrollInsuranceProfileModule),
      },

      {
        path: 'my-nominee',
        loadChildren: () =>
          import('./nominee-management-common/nominee-management.module').then(m => m.BitsHrPayrollNomineeManagementCommonModule),
      },
      {
        path: 'salary-payslip-admin',
        loadChildren: () =>
          import('app/payroll-management-system/salary-payslip-admin/salary-payslip-admin.module').then(
            m => m.BitsHrPayrollAppSalaryPayslipAdminModule
          ),
      },
      {
        path: 'income-tax-statement-admin',
        loadChildren: () =>
          import('app/payroll-management-system/income-tax-statement-admin/income-tax-statement-admin.module').then(
            m => m.BitsHrPayrollAppIncomeTaxStatementAdminModule
          ),
      },
      //user-tax-acknowledgement-receipt
      {
        path: 'user-tax-acknowledgement-receipt',
        data: {
          pageTitle: 'taxAcknowledgementReceipt.home.title',
        },
        loadChildren: () =>
          import('app/payroll-management-system/user-tax-acknowledgement-receipt/user-tax-acknowledgement-receipt.module').then(
            m => m.BitsHrPayrollTaxAcknowledgementReceiptModule
          ),
      },
      {
        path: 'procurement-requisition-user',
        data: {
          pageTitle: 'procurementRequisition.home.approval',
        },
        loadChildren: () => import('./entities/proc-req/proc-req.module').then(m => m.ProcReqModule),
      },
      //office-notices-archive
      {
        path: 'office-notices-archive',
        data: {
          pageTitle: 'bitsHrPayrollApp.officeNotices.home.archive',
        },
        loadChildren: () =>
          import('./dashboard/user-end-archive-notice/archive-office-notices.module').then(m => m.BitsHrPayrollArchiveOfficeNoticesModule),
      },
      //offer-archive
      {
        path: 'offer-archive',
        data: {
          pageTitle: 'bitsHrPayrollApp.offer.home.archive',
        },
        loadChildren: () => import('./dashboard/user-end-archive-offer/archive-offer.module').then(m => m.BitsHrPayrollArchiveOfferModule),
      },
    ]),
  ],
})
export class UserRouterModule {}
