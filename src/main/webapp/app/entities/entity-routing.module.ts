import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';
import { AttendanceTimeSheetAdminComponent } from '../attendance-management-system/ats/attendance-time-sheet-admin/attendance-time-sheet-admin.component';
import { ConfigCustomModule } from './config-custom/config-custom.module';
import {LeavePolicyManagementModule} from "./leave-policy-management/leave-policy-management.module";

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'attendance-time-sheet-admin',
        canActivate: [UserRouteAccessService],
        component: AttendanceTimeSheetAdminComponent,
        data: {
          pageTitle: 'attendance-time-sheet.titleAdmin',
        },
      },
      {
        path: 'designation',
        data: { pageTitle: 'bitsHrPayrollApp.designations.home' },
        loadChildren: () => import('./designation/designation.module').then(m => m.DesignationModule),
      },
      {
        path: 'nationality',
        data: { pageTitle: 'bitsHrPayrollApp.nationalities.home' },
        loadChildren: () => import('./nationality/nationality.module').then(m => m.NationalityModule),
      },
      {
        path: 'bank-branch',
        data: { pageTitle: 'bitsHrPayrollApp.bankBranch.home.title' },
        loadChildren: () => import('./bank-branch/bank-branch.module').then(m => m.BankBranchModule),
      },
      {
        path: 'unit-of-measurement',
        data: { pageTitle: 'bitsHrPayrollApp.unitOfMeasurementPRF.home.title' },
        loadChildren: () => import('./unit-of-measurement/unit-of-measurement.module').then(m => m.UnitOfMeasurementModule),
      },
      {
        path: 'time-slot',
        data: { pageTitle: 'bitsHrPayrollApp.timeSlot.home.title' },
        loadChildren: () => import('./time-slot/time-slot.module').then(m => m.TimeSlotModule),
      },
      {
        path: 'building',
        data: { pageTitle: 'bitsHrPayrollApp.building.home.title' },
        loadChildren: () => import('./building/building.module').then(m => m.BuildingModule),
      },
      {
        path: 'floor',
        data: { pageTitle: 'bitsHrPayrollApp.floor.home.title' },
        loadChildren: () => import('./floor/floor.module').then(m => m.FloorModule),
      },
      {
        path: 'leave-policy-management',
        data: { pageTitle: 'bitsHrPayrollApp.floor.home.title' },
        loadChildren: () => import('./leave-policy-management/leave-policy-management.module').then(m => m.LeavePolicyManagementModule),
      },
      {
        path: 'room-type',
        data: { pageTitle: 'bitsHrPayrollApp.roomType.home.title' },
        loadChildren: () => import('./room-type/room-type.module').then(m => m.RoomTypeModule),
      },
      {
        path: 'room',
        data: { pageTitle: 'bitsHrPayrollApp.room.home.title' },
        loadChildren: () => import('./room/room.module').then(m => m.RoomModule),
      },
      {
        path: 'unit',
        data: { pageTitle: 'bitsHrPayrollApp.units.home' },
        loadChildren: () => import('./unit/unit.module').then(m => m.UnitModule),
      },
      {
        path: 'band',
        data: { pageTitle: 'bitsHrPayrollApp.bands.home' },
        loadChildren: () => import('./band/band.module').then(m => m.BandModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'bitsHrPayrollApp.department.home' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'employee-custom',
        data: { pageTitle: 'bitsHrPayrollApp.employeeCustom.home.title' },
        loadChildren: () => import('./employee-custom/employee-custom.module').then(m => m.BitsHrPayrollAppEmployeeCustomModule),
      },
      {
        path: 'arrear-salary-master',
        data: { pageTitle: 'bitsHrPayrollApp.arrearSalaryMaster.home.title' },
        loadChildren: () => import('./arrear-salary-master/arrear-salary-master.module').then(m => m.ArrearSalaryMasterModule),
      },
      {
        path: 'arrear-salary-item',
        data: { pageTitle: 'bitsHrPayrollApp.arrearSalaryItem.home.title' },
        loadChildren: () => import('./arrear-salary-item/arrear-salary-item.module').then(m => m.ArrearSalaryItemModule),
      },
      {
        path: 'attendance',
        data: { pageTitle: 'bitsHrPayrollApp.attendance.home.title' },
        loadChildren: () => import('./attendance/attendance.module').then(m => m.AttendanceModule),
      },
      {
        path: 'attendance-entry',
        data: { pageTitle: 'bitsHrPayrollApp.manualAttendanceEntryUpdate.home.title' },
        loadChildren: () => import('./attendance-entry/attendance-entry.module').then(m => m.AttendanceEntryModule),
      },
      {
        path: 'attendance-summary',
        data: { pageTitle: 'bitsHrPayrollApp.attendanceSummary.home.title' },
        loadChildren: () => import('./attendance-summary/attendance-summary.module').then(m => m.AttendanceSummaryModule),
      },
      {
        path: 'education-details',
        data: { pageTitle: 'bitsHrPayrollApp.educationDetails.home.title' },
        loadChildren: () => import('./education-details/education-details.module').then(m => m.EducationDetailsModule),
      },
      {
        path: 'employee-noc',
        data: { pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title' },
        loadChildren: () => import('./employee-noc/employee-noc.module').then(m => m.EmployeeNOCModule),
      },
      {
        path: 'employee-pin',
        data: { pageTitle: 'bitsHrPayrollApp.employeePin.home.title' },
        loadChildren: () => import('./employee-pin/employee-pin.module').then(m => m.EmployeePinModule),
      },
      {
        path: 'employee-pin-configuration',
        data: { pageTitle: 'bitsHrPayrollApp.employeePinConfiguration.home.title' },
        loadChildren: () =>
          import('./employee-pin-configuration/employee-pin-configuration.module').then(m => m.EmployeePinConfigurationModule),
      },
      {
        path: 'employee-resignation',
        data: { pageTitle: 'bitsHrPayrollApp.employeeResignation.home.title' },
        loadChildren: () => import('./employee-resignation/employee-resignation.module').then(m => m.EmployeeResignationModule),
      },
      {
        path: 'employee-salary',
        data: { pageTitle: 'bitsHrPayrollApp.employeeSalary.home.title' },
        loadChildren: () => import('./employee-salary/employee-salary.module').then(m => m.EmployeeSalaryModule),
      },
      {
        path: 'employee-salary-temp-data',
        data: { pageTitle: 'bitsHrPayrollApp.employeeSalaryTempData.home.title' },
        loadChildren: () =>
          import('./employee-salary-temp-data/employee-salary-temp-data.module').then(m => m.EmployeeSalaryTempDataModule),
      },
      {
        path: 'employee-static-file',
        data: { pageTitle: 'bitsHrPayrollApp.employeeStaticFile.home.title' },
        loadChildren: () => import('./employee-static-file/employee-static-file.module').then(m => m.EmployeeStaticFileModule),
      },
      {
        path: 'employment-certificate',
        data: { pageTitle: 'bitsHrPayrollApp.employmentCertificate.home.title' },
        loadChildren: () => import('./employment-certificate/employment-certificate.module').then(m => m.EmploymentCertificateModule),
      },
      {
        path: 'employment-history',
        data: { pageTitle: 'bitsHrPayrollApp.employmentHistory.home.title' },
        loadChildren: () => import('./employment-history/employment-history.module').then(m => m.EmploymentHistoryModule),
      },
      {
        path: 'event-log',
        data: { pageTitle: 'bitsHrPayrollApp.eventLog.home.title' },
        loadChildren: () => import('./event-log/event-log.module').then(m => m.EventLogModule),
      },
      {
        path: 'festival',
        data: { pageTitle: 'bitsHrPayrollApp.festival.home.title' },
        loadChildren: () => import('./festival/festival.module').then(m => m.FestivalModule),
      },
      {
        path: 'festival-bonus-details',
        data: { pageTitle: 'bitsHrPayrollApp.festivalBonusDetails.home.title' },
        loadChildren: () => import('./festival-bonus-details/festival-bonus-details.module').then(m => m.FestivalBonusDetailsModule),
      },
      {
        path: 'final-settlement',
        data: { pageTitle: 'bitsHrPayrollApp.finalSettlement.home.title' },
        loadChildren: () => import('./final-settlement/final-settlement.module').then(m => m.FinalSettlementModule),
      },
      {
        path: 'flex-schedule',
        data: { pageTitle: 'bitsHrPayrollApp.flexSchedule.home.title' },
        loadChildren: () => import('./flex-schedule/flex-schedule.module').then(m => m.FlexScheduleModule),
      },
      {
        path: 'flex-schedule-application',
        data: { pageTitle: 'bitsHrPayrollApp.flexScheduleApplication.home.title' },
        loadChildren: () =>
          import('./flex-schedule-application/flex-schedule-application.module').then(m => m.FlexScheduleApplicationModule),
      },
      {
        path: 'hold-fb-disbursement',
        data: { pageTitle: 'bitsHrPayrollApp.holdFbDisbursement.home.title' },
        loadChildren: () => import('./hold-fb-disbursement/hold-fb-disbursement.module').then(m => m.HoldFbDisbursementModule),
      },
      {
        path: 'hold-salary-disbursement',
        data: { pageTitle: 'bitsHrPayrollApp.holdSalaryDisbursement.home.title' },
        loadChildren: () => import('./hold-salary-disbursement/hold-salary-disbursement.module').then(m => m.HoldSalaryDisbursementModule),
      },
      {
        path: 'ait-config',
        data: { pageTitle: 'bitsHrPayrollApp.aitConfig.home.title' },
        loadChildren: () => import('./ait-config/ait-config.module').then(m => m.AitConfigModule),
      },
      {
        path: 'ait-payment',
        data: { pageTitle: 'bitsHrPayrollApp.aitPayment.home.title' },
        loadChildren: () => import('./ait-payment/ait-payment.module').then(m => m.AitPaymentModule),
      },
      {
        path: 'income-tax-challan',
        data: { pageTitle: 'bitsHrPayrollApp.incomeTaxChallan.home.title' },
        loadChildren: () => import('./income-tax-challan/income-tax-challan.module').then(m => m.IncomeTaxChallanModule),
      },
      {
        path: 'insurance-registration',
        data: { pageTitle: 'bitsHrPayrollApp.insuranceRegistration.home.title' },
        loadChildren: () => import('./insurance-registration/insurance-registration.module').then(m => m.InsuranceRegistrationModule),
      },
      {
        path: 'insurance-claim',
        data: { pageTitle: 'bitsHrPayrollApp.insuranceClaim.home.title' },
        loadChildren: () => import('./insurance-claim/insurance-claim.module').then(m => m.InsuranceClaimModule),
      },
      {
        path: 'pf-account',
        data: { pageTitle: 'bitsHrPayrollApp.pfAccount.home.title' },
        loadChildren: () => import('./pf-account/pf-account.module').then(m => m.PfAccountModule),
      },
      {
        path: 'pf-collection',
        data: { pageTitle: 'bitsHrPayrollApp.pfCollection.home.title' },
        loadChildren: () => import('./pf-collection/pf-collection.module').then(m => m.PfCollectionModule),
      },
      {
        path: 'pf-loan-application',
        data: { pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title' },
        loadChildren: () => import('./pf-loan-application/pf-loan-application.module').then(m => m.PfLoanApplicationModule),
      },
      {
        path: 'pf-loan',
        data: { pageTitle: 'bitsHrPayrollApp.pfLoan.home.title' },
        loadChildren: () => import('./pf-loan/pf-loan.module').then(m => m.PfLoanModule),
      },
      {
        path: 'pf-loan-repayment',
        data: { pageTitle: 'bitsHrPayrollApp.pfLoanRepayment.home.title' },
        loadChildren: () => import('./pf-loan-repayment/pf-loan-repayment.module').then(m => m.PfLoanRepaymentModule),
      },
      {
        path: 'pf-nominee',
        data: { pageTitle: 'bitsHrPayrollApp.pfNominee.home.title' },
        loadChildren: () => import('./pf-nominee/pf-nominee.module').then(m => m.PfNomineeModule),
      },
      {
        path: 'item-information',
        data: { pageTitle: 'bitsHrPayrollApp.itemInformationPRF.home.title' },
        loadChildren: () => import('./item-information/item-information.module').then(m => m.ItemInformationModule),
      },
      {
        path: 'proc-req-master',
        data: { pageTitle: 'bitsHrPayrollApp.procReqMaster.home.title' },
        loadChildren: () => import('./proc-req-master/proc-req-master.module').then(m => m.ProcReqMasterModule),
      },
      {
        path: 'proc-req',
        data: { pageTitle: 'bitsHrPayrollApp.procReq.home.title' },
        loadChildren: () => import('./proc-req/proc-req.module').then(m => m.ProcReqModule),
      },
      {
        path: 'pro-rata-festival-bonus',
        data: { pageTitle: 'bitsHrPayrollApp.proRataFestivalBonus.home.title' },
        loadChildren: () => import('./pro-rata-festival-bonus/pro-rata-festival-bonus.module').then(m => m.ProRataFestivalBonusModule),
      },
      {
        path: 'recruitment-requisition-form',
        data: { pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title' },
        loadChildren: () =>
          import('./recruitment-requisition-form/recruitment-requisition-form.module').then(m => m.RecruitmentRequisitionFormModule),
      },
      {
        path: 'references',
        data: { pageTitle: 'bitsHrPayrollApp.referencesTitle.home.title' },
        loadChildren: () => import('./references/references.module').then(m => m.ReferencesModule),
      },
      {
        path: 'deduction-type',
        data: { pageTitle: 'bitsHrPayrollApp.deductionType.home.title' },
        loadChildren: () => import('./deduction-type/deduction-type.module').then(m => m.DeductionTypeModule),
      },
      {
        path: 'salary-certificate',
        data: { pageTitle: 'bitsHrPayrollApp.salaryCertificate.home.title' },
        loadChildren: () => import('./salary-certificate/salary-certificate.module').then(m => m.SalaryCertificateModule),
      },
      {
        path: 'salary-deduction',
        data: { pageTitle: 'bitsHrPayrollApp.salaryDeduction.home.title' },
        loadChildren: () => import('./salary-deduction/salary-deduction.module').then(m => m.SalaryDeductionModule),
      },
      {
        path: 'special-shift-timing',
        data: { pageTitle: 'bitsHrPayrollApp.specialShiftTiming.home.title' },
        loadChildren: () => import('./special-shift-timing/special-shift-timing.module').then(m => m.SpecialShiftTimingModule),
      },
      {
        path: 'tax-acknowledgement-receipt',
        data: { pageTitle: 'bitsHrPayrollApp.taxAcknowledgementReceipt.home.title' },
        loadChildren: () =>
          import('./tax-acknowledgement-receipt/tax-acknowledgement-receipt.module').then(m => m.TaxAcknowledgementReceiptModule),
      },
      {
        path: 'tax-acknowledgement-receipt-finance',
        loadChildren: () =>
          import('./tax-acknowledgement-receipt/tax-acknowledgement-receipt.module').then(m => m.TaxAcknowledgementReceiptModule),
      },
      {
        path: 'training-history',
        data: { pageTitle: 'bitsHrPayrollApp.trainingHistoryTitle.home.title' },
        loadChildren: () => import('./training-history/training-history.module').then(m => m.TrainingHistoryModule),
      },
      {
        path: 'vehicle',
        data: { pageTitle: 'bitsHrPayrollApp.vehicle.home.title' },
        loadChildren: () => import('./vehicle/vehicle.module').then(m => m.VehicleModule),
      },
      {
        path: 'vehicle-requisition',
        data: { pageTitle: 'bitsHrPayrollApp.vehicleRequisition.home.title' },
        loadChildren: () => import('./vehicle-requisition/vehicle-requisition.module').then(m => m.VehicleRequisitionModule),
      },
      {
        path: 'user-feedback',
        data: { pageTitle: 'bitsHrPayrollApp.userFeedback.home.title' },
        loadChildren: () => import('./user-feedback/user-feedback.module').then(m => m.UserFeedbackModule),
      },
      {
        path: 'area',
        data: { pageTitle: 'bitsHrPayrollApp.area.home.title' },
        loadChildren: () => import('./area/area.module').then(m => m.AreaModule),
      },
      {
        path: 'arrear-payment',
        data: { pageTitle: 'bitsHrPayrollApp.arrearPayment.home.title' },
        loadChildren: () => import('./arrear-payment/arrear-payment.module').then(m => m.ArrearPaymentModule),
      },
      {
        path: 'arrear-salary',
        data: { pageTitle: 'bitsHrPayrollApp.arrearSalary.home.title' },
        loadChildren: () => import('./arrear-salary/arrear-salary.module').then(m => m.ArrearSalaryModule),
      },
      {
        path: 'attendance-sync-cache',
        data: { pageTitle: 'bitsHrPayrollApp.attendanceSyncCache.home.title' },
        loadChildren: () => import('./attendance-sync-cache/attendance-sync-cache.module').then(m => m.AttendanceSyncCacheModule),
      },
      {
        path: 'config',
        data: { pageTitle: 'bitsHrPayrollApp.config.home.title' },
        loadChildren: () => import('./config/config.module').then(m => m.ConfigModule),
      },
      {
        path: 'config-custom',
        data: { pageTitle: 'bitsHrPayrollApp.config.home.title' },
        loadChildren: () => import('./config-custom/config-custom.module').then(m => m.ConfigCustomModule),
      },
      {
        path: 'calender-year',
        data: { pageTitle: 'bitsHrPayrollApp.calenderYear.home.title' },
        loadChildren: () => import('./calender-year/calender-year.module').then(m => m.CalenderYearModule),
      },
      {
        path: 'festival-bonus-config',
        data: { pageTitle: 'bitsHrPayrollApp.festivalBonusConfig.home.title' },
        loadChildren: () => import('./festival-bonus-config/festival-bonus-config.module').then(m => m.FestivalBonusConfigModule),
      },
      {
        path: 'file-templates',
        data: { pageTitle: 'bitsHrPayrollApp.userFileTemplates.home' },
        loadChildren: () => import('./file-templates/file-templates.module').then(m => m.FileTemplatesModule),
      },
      {
        path: 'holidays',
        data: { pageTitle: 'bitsHrPayrollApp.holidays.home.title' },
        loadChildren: () => import('./holidays/holidays.module').then(m => m.HolidaysModule),
      },
      {
        path: 'leave-balance',
        data: { pageTitle: 'bitsHrPayrollApp.leaveBalance.home.title' },
        loadChildren: () => import('./leave-balance/leave-balance.module').then(m => m.LeaveBalanceModule),
      },
      {
        path: 'work-from-home-application',
        data: { pageTitle: 'bitsHrPayrollApp.workFromHomeApplication.home' },
        loadChildren: () =>
          import('./work-from-home-application/work-from-home-application.module').then(m => m.WorkFromHomeApplicationModule),
      },
      {
        path: 'individual-arrear-salary',
        data: { pageTitle: 'bitsHrPayrollApp.individualArrearSalary.home.title' },
        loadChildren: () => import('./individual-arrear-salary/individual-arrear-salary.module').then(m => m.IndividualArrearSalaryModule),
      },
      {
        path: 'working-experience',
        data: { pageTitle: 'bitsHrPayrollApp.workingExperience.home' },
        loadChildren: () => import('./working-experience/working-experience.module').then(m => m.WorkingExperienceModule),
      },
      {
        path: 'pf-arrear',
        data: { pageTitle: 'bitsHrPayrollApp.pfArrear.home.title' },
        loadChildren: () => import('./pf-arrear/pf-arrear.module').then(m => m.PfArrearModule),
      },
      {
        path: 'leave-allocation',
        data: { pageTitle: 'bitsHrPayrollApp.leaveAllocation.home.title' },
        loadChildren: () => import('./leave-allocation/leave-allocation.module').then(m => m.LeaveAllocationModule),
      },
      {
        path: 'mobile-bill',
        data: { pageTitle: 'bitsHrPayrollApp.mobileBill.home.title' },
        loadChildren: () => import('./mobile-bill/mobile-bill.module').then(m => m.MobileBillModule),
      },
      {
        path: 'salary-generator-master',
        data: { pageTitle: 'bitsHrPayrollApp.salaryGeneratorMaster.home.title' },
        loadChildren: () => import('./salary-generator-master/salary-generator-master.module').then(m => m.SalaryGeneratorMasterModule),
      },
      {
        path: 'offer',
        data: { pageTitle: 'bitsHrPayrollApp.offer.home.title' },
        loadChildren: () => import('./offer/offer.module').then(m => m.OfferModule),
      },
      {
        path: 'office-notices',
        data: { pageTitle: 'bitsHrPayrollApp.officeNotices.home.title' },
        loadChildren: () => import('./office-notices/office-notices.module').then(m => m.OfficeNoticesModule),
      },
      {
        path: 'movement-entry',
        data: { pageTitle: 'bitsHrPayrollApp.movementEntry.home.title' },
        loadChildren: () => import('./movement-entry/movement-entry.module').then(m => m.MovementEntryModule),
      },
      {
        path: 'room-requisition',
        data: { pageTitle: 'bitsHrPayrollApp.roomRequisition.home.title' },
        loadChildren: () => import('./room-requisition/room-requisition.module').then(m => m.RoomRequisitionModule),
      },
      {
        path: 'leave-application',
        data: { pageTitle: 'bitsHrPayrollApp.leaveApplication.home.title' },
        loadChildren: () => import('./leave-application/leave-application.module').then(m => m.LeaveApplicationModule),
      },
      {
        path: 'organization',
        data: { pageTitle: 'bitsHrPayrollApp.organization.home.title' },
        loadChildren: () => import('./organization/organization.module').then(m => m.OrganizationModule),
      },
      {
        path: 'insurance-configuration',
        data: { pageTitle: 'bitsHrPayrollApp.insuranceConfiguration.home.title' },
        loadChildren: () => import('./insurance-configuration/insurance-configuration.module').then(m => m.InsuranceConfigurationModule),
      },
      {
        path: 'nominee',
        data: { pageTitle: 'bitsHrPayrollApp.nominee.home.title' },
        loadChildren: () => import('./nominee-admin/nominee.module').then(m => m.BitsHrPayrollNomineeModule),
      },

      {
        path: 'movement-entry',
        data: { pageTitle: 'bitsHrPayrollApp.employee.home.title' },
        loadChildren: () => import('./movement-entry/movement-entry.module').then(m => m.MovementEntryModule),
      },
      {
        path: 'employee-docs-admin',
        data: { pageTitle: 'bitsHrPayrollApp.employee.home.title' },
        loadChildren: () => import('./employment-doc-admin/employee-doc-admin.module').then(m => m.BitsHrPayrollEmployeeDOCsAdminModule),
      },
      {
        path: 'procurement-requisition-manage',
        data: { pageTitle: 'bitsHrPayrollApp.procurementRequisitionManage.home.title' },
        loadChildren: () => import('./proc-req-master/proc-req-master.module').then(m => m.ProcReqMasterModule),
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.BitsHrPayrollLocationModule),
      },

      {
        path: 'employee-document',
        data: { pageTitle: 'bitsHrPayrollApp.employeeDocument.home.title' },
        loadChildren: () => import('./employee-document/employee-document.module').then(m => m.EmployeeDocumentModule),
      },
      {
        path: 'recruitment-requisition-budget',
        data: { pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionBudget.home.title' },
        loadChildren: () =>
          import('./recruitment-requisition-budget/recruitment-requisition-budget.module').then(m => m.RecruitmentRequisitionBudgetModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'bitsHrPayrollApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
