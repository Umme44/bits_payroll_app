import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { SalaryGenerationService } from './salary-generation.service';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { SalaryGenerationUtilService } from './salary-generation-util.Service';
import Swal from 'sweetalert2';
import { ISalaryGenerationPreValidation } from 'app/shared/model/salary-generation-pre-validation.model';
import { ISalaryGenerationModel, SalaryGenerationModel } from '../../shared/model/salary-generation.model';
import { EmployeeSalary, IEmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';
import { SalaryLockService } from '../../shared/legacy/legacy-service/salary-lock-service';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';
import { MobileBillService } from '../../shared/legacy/legacy-service/mobile-bill.service';
import { AttendanceSummaryService } from '../../shared/legacy/legacy-service/attendance-summary.service';
import { PfLoanRepaymentService } from '../../shared/legacy/legacy-service/pf-loan-repayment.service';
import { ISalaryGeneratorMaster } from '../../shared/legacy/legacy-model/salary-generator-master.model';
import { SalaryGeneratorMasterService } from '../../shared/legacy/legacy-service/salary-generator-master.service';

// noinspection DuplicatedCode,JSUnusedLocalSymbols
@Component({
  selector: 'jhi-salary-generation',
  templateUrl: './salary-generation.component.html',
  styleUrls: ['salary-generation.component.scss'],
})
export class SalaryGenerationComponent implements OnInit {
  salaryGenerationModel: ISalaryGenerationModel;
  employeeSalaries?: IEmployeeSalary[];
  salaryGeneratorMasters: ISalaryGeneratorMaster[] = [];
  salaries?: ISalaryGeneratorMaster;

  employees?: String[];
  ready = false;
  currentYear: number = new Date().getFullYear();
  years: number[];
  months = [
    { Value: 1, Text: 'January' },
    { Value: 2, Text: 'February' },
    { Value: 3, Text: 'March' },
    { Value: 4, Text: 'April' },
    { Value: 5, Text: 'May' },
    { Value: 6, Text: 'June' },
    { Value: 7, Text: 'July' },
    { Value: 8, Text: 'August' },
    { Value: 9, Text: 'September' },
    { Value: 10, Text: 'October' },
    { Value: 11, Text: 'November' },
    { Value: 12, Text: 'December' },
  ];

  mobileBillUploadSuccess: boolean | null = false;
  errorOnMobileBillUpload: boolean | null = false;

  errorAttendanceUpload: boolean | null = false;
  attendanceUploadSuccess: boolean | null = false;

  errorDeductionUpload: boolean | null = false;
  deductionUploadSuccess: boolean | null = false;

  alreadyUploadedSalaryGen: boolean | null = false;

  viewAlreadyUploadedMobileBill: boolean | null = false;
  viewAlreadyUploadedAttendance: boolean | null = false;
  viewAlreadyUploadedDeductions: boolean | null = false;
  uploadReady: boolean | null = false;
  uploadMobileReady: boolean | null = false;
  uploadAttendanceReady: boolean | null = false;
  uploadPfReady: boolean | null = false;
  mobImportBtnTxt: String = 'Import';
  attendanceImportBtnTxt: String = 'Import';
  otherDeductionImportBtnTxt: String = 'Import';
  isAttendanceCovered = true;
  nonCoveringAttendance = '';
  salaryGenerationPreValidation!: ISalaryGenerationPreValidation;
  editForm = this.fb.group({
    year: [],
    month: [],
  });
  private fileToUpload: any;
  private mobileFileToUpload: any;
  private attendanceFileToUpload: any;
  private deductionFileToUpload: any;

  isAttendanceGenerationOnProgress = false;

  constructor(
    protected salaryGenerationService: SalaryGenerationService,
    protected salaryGenerationUtilService: SalaryGenerationUtilService,
    protected salaryGeneratorMasterService: SalaryGeneratorMasterService,
    protected employeeService: EmployeeService,
    protected mobileBillService: MobileBillService,
    protected salaryLockService: SalaryLockService,
    protected fb: FormBuilder,
    protected pfLoanRepaymentService: PfLoanRepaymentService,
    protected attendanceSummaryService: AttendanceSummaryService,
    protected router: Router
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];
    this.salaryGenerationModel = new SalaryGenerationModel();
  }

  ngOnInit(): void {
    this.editForm.patchValue({
      year: this.years[0],
      month: this.months[0].Value,
    });
  }

  trackId(index: number, item: IEmployeeSalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  generate(): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Salary generation process will be executed!',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes!',
    }).then(result => {
      if (result.isConfirmed) {
        this.startSalaryGenerationOnProcessModal();
        this.salaryGenerationService
          .generateSalary(this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
          // eslint-disable-next-line @typescript-eslint/no-unused-vars
          .subscribe(
            (resp: HttpResponse<EmployeeSalary[]>) => {
              Swal.close();
              this.showSalaryGenerationSuccessModal();
              this.router.navigate(['employee-salary', this.editForm.get(['year'])?.value, this.editForm.get(['month'])?.value]);
            },
            err => {
              this.showErrorModal();
            }
          );
      }
    });
  }

  importXlsx(ev: any, type: string): void {
    if (ev.target.files.length === 0) return;
    switch (type) {
      case 'mobile':
        this.mobileFileToUpload = ev.target.files[0];
        this.uploadMobileBill();
        this.uploadMobileReady = false;
        break;
      case 'salary-deductions':
        this.deductionFileToUpload = ev.target.files[0];
        this.uploadDeductions();
        this.uploadPfReady = false;
        break;
      case 'attendance':
        this.attendanceFileToUpload = ev.target.files[0];
        this.uploadAttendance();
        this.uploadAttendanceReady = false;
        break;
    }
  }

  /*checkSalaryIsLocked():boolean{
    //make an api for month and year

    return ;
  }*/

  clickNext(): void {
    this.ready = !this.ready;
    this.salaryGenerationUtilService
      .getSalaryGenerationPreValidation(this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
      .subscribe((resp: HttpResponse<ISalaryGenerationPreValidation>) => {
        this.salaryGenerationPreValidation = resp.body!;
      });

    //check selected month salary is locked
    this.salaryLockService.isSalaryLocked(this.editForm.get(['month'])!.value, this.editForm.get(['year'])!.value).subscribe(isLocked => {
      if (isLocked.body && isLocked.body === true) {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'This Month Salary is Locked!',
        });
        this.ready = false;
        return;
      } else {
        this.salaryGeneratorMasterService.query().subscribe((res: HttpResponse<ISalaryGeneratorMaster[]>) => {
          // eslint-disable-next-line no-console
          console.log(res.body);
          this.salaryGeneratorMasters = res.body || [];
          // eslint-disable-next-line no-console
          console.log(this.editForm.get(['month'])!.value + ' ' + this.editForm.get(['year'])!.value);
          const month: string = this.editForm.get(['month'])!.value + '';
          const year: string = this.editForm.get(['year'])!.value + '';
          if (this.salaryGeneratorMasters !== undefined && this.salaryGeneratorMasters.length > 0) {
            this.salaries = this.salaryGeneratorMasters.filter(x => x.month === month && x.year === year)[0];
            // eslint-disable-next-line no-console
            console.log(this.salaries);
          }

          if (this.salaries != null) {
            this.alreadyUploadedSalaryGen = true;

            if (this.salaries.isMobileBillImported === true) {
              this.mobImportBtnTxt = 'Re-Import';
              this.viewAlreadyUploadedMobileBill = true;
            }

            if (this.salaries.isAttendanceImported === true) {
              this.attendanceImportBtnTxt = 'Re-Import';
              this.viewAlreadyUploadedAttendance = true;
              this.salaryGenerationUtilService
                .checkRemainingEmployeesAttendanceSummary(this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
                .subscribe((response: HttpResponse<String[]>) => {
                  this.employees = response.body || [];
                  this.isAttendanceCovered = true;
                  if (this.employees?.length > 0) {
                    this.isAttendanceCovered = false;
                    this.nonCoveringAttendance = this.employees?.toString();
                  }
                });
            }

            if (this.salaries.isSalaryDeductionImported === true) {
              this.otherDeductionImportBtnTxt = 'Re-Import';
              this.viewAlreadyUploadedDeductions = true;
            }
          }
        });
      }
    });
  }

  private uploadMobileBill(): void {
    this.startFileUploadingOnProcessModal();
    this.salaryGenerationService
      .uploadMobileBills(this.mobileFileToUpload, this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
      .subscribe((resp: HttpResponse<boolean>) => {
        Swal.close();
        this.mobileBillUploadSuccess = resp.body;
        this.mobImportBtnTxt = 'Re Import';
        if (this.mobileBillUploadSuccess === false) {
          this.showBadDataFormatModal();
          this.errorOnMobileBillUpload = true;
          this.viewAlreadyUploadedMobileBill = false;
        } else {
          this.showFileProcessSuccesModal();
        }
      });
  }

  private uploadAttendance(): void {
    this.startFileUploadingOnProcessModal();
    this.salaryGenerationService
      .uploadLeaveAttendance(this.attendanceFileToUpload, this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
      .subscribe((resp: HttpResponse<boolean>) => {
        Swal.close();
        this.attendanceUploadSuccess = resp.body;
        this.attendanceImportBtnTxt = 'Re Import';

        if (this.attendanceUploadSuccess === false) {
          this.showBadDataFormatModal();
          this.errorAttendanceUpload = true;
          this.viewAlreadyUploadedAttendance = false;
        } else {
          this.showFileProcessSuccesModal();
        }

        this.salaryGenerationUtilService
          .checkRemainingEmployeesAttendanceSummary(this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
          .subscribe((res: HttpResponse<String[]>) => {
            this.employees = res.body || [];
            if (this.employees?.length > 0) {
              this.isAttendanceCovered = false;
              this.nonCoveringAttendance = this.employees?.toString();
            } else {
              this.isAttendanceCovered = true;
            }
          });
      });
  }

  private uploadDeductions(): void {
    this.startFileUploadingOnProcessModal();
    this.salaryGenerationService
      .uploadOtherDeductions(this.deductionFileToUpload, this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
      .subscribe((resp: HttpResponse<boolean>) => {
        Swal.close();
        this.deductionUploadSuccess = resp.body;
        this.otherDeductionImportBtnTxt = 'Re Import';
        if (this.deductionUploadSuccess === false) {
          this.showBadDataFormatModal();
          this.errorDeductionUpload = true;
          this.viewAlreadyUploadedDeductions = false;
        } else {
          this.showFileProcessSuccesModal();
        }
      });
  }

  generateAttendanceSummary(): void {
    this.startFileUploadingOnProcessModal();
    this.isAttendanceGenerationOnProgress = true;
    this.salaryGenerationUtilService
      .generateAttendanceSummary(this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
      .subscribe((response: HttpResponse<boolean>) => {
        Swal.close();
        this.isAttendanceGenerationOnProgress = false;
        this.attendanceUploadSuccess = response.body;

        if (this.attendanceUploadSuccess === false) {
          this.showBadDataFormatModal();
          this.errorAttendanceUpload = true;
          this.viewAlreadyUploadedAttendance = false;
        } else {
          this.showFileProcessSuccesModal();
        }

        this.salaryGenerationUtilService
          .checkRemainingEmployeesAttendanceSummary(this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
          .subscribe((res: HttpResponse<String[]>) => {
            this.employees = res.body || [];
            if (this.employees?.length > 0) {
              this.isAttendanceCovered = false;
              this.nonCoveringAttendance = this.employees?.toString();
            }
          });
      });
  }

  startFileUploadingOnProcessModal(): void {
    Swal.fire({
      html: 'Your file is getting processed',
      allowOutsideClick: false,
      timerProgressBar: true,
      didOpen(): void {
        Swal.showLoading();
      },
    });
  }

  showBadDataFormatModal(): void {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Bad data format!',
    });
  }

  showFileProcessSuccesModal(): void {
    Swal.fire({
      icon: 'success',
      title: 'Success',
      text: 'Your file has been processed !',
      showConfirmButton: false,
      timer: 1500,
    });
  }

  startSalaryGenerationOnProcessModal(): void {
    Swal.fire({
      html: 'Generating salary',
      allowOutsideClick: false,
      timerProgressBar: true,
      didOpen(): void {
        Swal.showLoading();
      },
    });
  }

  showSalaryGenerationSuccessModal(): void {
    Swal.fire({
      icon: 'success',
      title: 'Success',
      text: 'Salary generated successfully !',
      showConfirmButton: false,
      timer: 1500,
    });
  }

  showErrorModal(): void {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Something went wrong!',
      footer: '<a>Failed to process your request!</a>',
    });
  }
}
