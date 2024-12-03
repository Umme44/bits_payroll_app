import { LOCALE_ID, NgModule } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/en';
import { BrowserModule, Title } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import dayjs from 'dayjs/esm';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { SharedModule } from 'app/shared/shared.module';
import { TranslationModule } from 'app/shared/language/translation.module';
import { AppRoutingModule } from './app-routing.module';
import { HomeModule } from './home/home.module';
// import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import { httpInterceptorProviders } from 'app/core/interceptor/index';
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

import { SearchModalComponent } from 'app/shared/specialized-search/search-modal/search-modal.component';
import { EmployeeAddressBookComponent } from './common/employee-address-book/employee-address-book.component';
import { SaveComponentComponent } from './shared/save-component/save-component.component';
import { BloodBankComponent } from './common/blood-bank/blood-bank.component';
import { AttendanceTimeSheetAdminComponent } from './attendance-management-system/ats/attendance-time-sheet-admin/attendance-time-sheet-admin.component';
import { AttendanceTimeSheetMyTeamComponent } from './attendance-management-system/ats/attendance-time-sheet-admin/attendance-time-sheet-my-team.component';
import { SuccessMessageComponent } from './shared/success-message/success-message.component';
import { BitsHrPayrollAppSalaryGenerationModule } from './payroll-management-system/salary-generation';
import { BitsHrPayrollAppSingleSalaryGenerationModule } from './payroll-management-system/single-salary-generation';
import { BitsHrPayrollAppUserAttendanceEntryModule } from './attendance-management-system/unused-user-attendance-entry';
import { BitsHrPayrollAppLeaveSummaryEndUserViewModule } from './attendance-management-system/leave-summary-end-user-view';
import { BitsHrPayrollAppAttendanceTimeSheetModule } from './attendance-management-system/ats/attendance-time-sheet';
import { BitsHrPayrollAppMonthlyAttendanceTimeSheetModule } from './attendance-management-system/ats/monthly-attendance-time-sheet';
import { BitsHrPayrollAppMonthlySalaryPaySlipModule } from './payroll-management-system/monthly-salary-pay-slip';
import { BitsHrPayrollAppUserPayslipModule } from './payroll-management-system/user-payslip';
import { BitsHrPayrollAppEmploymentActionsModule } from './payroll-management-system/employment-actions';
import { BitsHrPayrollAppIncomeTaxStatementModule } from './payroll-management-system/income-tax-statement';
import { BitsHrPayrollAppIncomeTaxReportExcelExportModule } from './payroll-management-system/income-tax-report-export-excel/income-tax-report-excel-export.module';
import { BitsHrPayrollAttendanceTimeSheetModule } from './attendance-management-system/ats/attendance-time-sheet.module';
import { BitsHrPayrollHeaderModule } from './layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from './shared/search-text-box/search-text-box.module';
import { BitsHrPayrollSelectEmployeeFormModule } from './shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollAppEmploymentHistoriesModule } from './payroll-management-system/employment-histories';
import { BitsHrPayrollAppUpcomingEventContractEndModule } from './payroll-management-system/upcoming-event-contract-end';
import { BitsHrPayrollAppUpcomingEventProbationEndModule } from './payroll-management-system/upcoming-event-probation-end';
import { BitsHrPayrollAppLeaveApprovalHrdModule } from './attendance-management-system/leave-approval-hrd';
import { BitsHrPayrollAppImportDataModule } from './admin/import-data';
import { BitsHrPayrollAppExportReportsModule } from './admin/export-reports';
import { BitsHrPayrollProvidentFundManagementModule } from './provident-fund-management/provident-fund-management.module';
import { BitsHrPayrollAppUserFeedbackRatingModule } from './common/user-rating';
import { BitsHrPayrollAppIncomeTaxReportsModule } from './payroll-management-system/income-tax-reports/income-tax-reports.module';
import { BitsHrPayrollAppUserEditAccountInformationModule } from './common/user-edit-account-information';
import { SalaryCertificateModule } from './entities/salary-certificate/salary-certificate.module';

@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
    HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    // SweetAlert2Module,
    BitsHrPayrollAppSalaryGenerationModule,
    BitsHrPayrollAppSingleSalaryGenerationModule,
    BitsHrPayrollAppExportReportsModule,
    BitsHrPayrollAppUserAttendanceEntryModule,
    BitsHrPayrollAppLeaveSummaryEndUserViewModule,
    BitsHrPayrollAppAttendanceTimeSheetModule,
    BitsHrPayrollAppMonthlyAttendanceTimeSheetModule,
    BitsHrPayrollAppMonthlySalaryPaySlipModule,
    BitsHrPayrollAppUserPayslipModule,
    BitsHrPayrollAppImportDataModule,
    BitsHrPayrollAppEmploymentActionsModule,
    // BitsHrPayrollAppEmployeeCustomModule,
    BitsHrPayrollAppIncomeTaxStatementModule,
    BitsHrPayrollAppIncomeTaxReportsModule,
    BitsHrPayrollAppIncomeTaxReportExcelExportModule,
    // BitsHrPayrollAppUserLeaveApplicationModule,
    // BitsHrPayrollAppHolidayCalendarModule,
    BitsHrPayrollAppLeaveApprovalHrdModule,
    // BitsHrPayrollAppLeaveApprovalSuperordinateModule,
    // BitsHrPayrollAppUserLeaveApplicationStatusAndHistoryModule,
    BitsHrPayrollAppUserEditAccountInformationModule,
    // BitsHrPayrollAppSimplifiedEmployeeFormModule,
    BitsHrPayrollAppUpcomingEventProbationEndModule,
    BitsHrPayrollAppUpcomingEventContractEndModule,
    BitsHrPayrollAppEmploymentHistoriesModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    // BitsHrPayrollEntityModule,
    // BitsHrPayrollUserFeaturesModule,
    BitsHrPayrollProvidentFundManagementModule,
    // BitsHrPayrollAppRoutingModule,
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollSearchTextBoxModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollAttendanceTimeSheetModule,
    BitsHrPayrollAppUserFeedbackRatingModule,
    FormsModule,
    NgSelectModule,
    // NgOptionHighlightModule,
    // BitsHrPayrollSelectPfAccountFormModule,
    // BitsHrPayrollNomineeManagementCommonModule,
    // BitsHrPayrollApprovalFeaturesModule,
    // BitsHrPayrollNoDataFoundModule,
    SalaryCertificateModule,

    // jhipster-needle-angular-add-module JHipster will add new module here

    AppRoutingModule,
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-', caseSensitive: true }),
    TranslationModule,
  ],
  providers: [
    Title,
    { provide: LOCALE_ID, useValue: 'en' },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    httpInterceptorProviders,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    SearchModalComponent,

    // --- custom components ---

    EmployeeAddressBookComponent,
    SaveComponentComponent,
    SearchModalComponent,
    BloodBankComponent,
    SuccessMessageComponent,
    AttendanceTimeSheetAdminComponent,
    AttendanceTimeSheetMyTeamComponent,

    // --- custom components ---
  ],
  bootstrap: [MainComponent],
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
