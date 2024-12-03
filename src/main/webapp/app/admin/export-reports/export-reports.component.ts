import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ExportReportsService } from 'app/admin/export-reports/export-reports.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';
import {
  swalClose,
  swalForWarningWithMessage,
  swalOnBatchDeleteConfirmation,
  swalOnLoading,
  swalOnRequestError,
} from 'app/shared/swal-common/swal-common';
import { DATE_FORMAT } from '../../config/input.constants';
import { DepartmentService } from '../../shared/legacy/legacy-service/department.service';
import { DesignationService } from '../../shared/legacy/legacy-service/designation.service';
import { UnitService } from '../../shared/legacy/legacy-service/unit.service';
import { IDepartment } from '../../shared/legacy/legacy-model/department.model';
import { IDesignation } from '../../shared/legacy/legacy-model/designation.model';
import { IUnit } from '../../shared/legacy/legacy-model/unit.model';

@Component({
  selector: 'jhi-export-reports',
  templateUrl: './export-reports.component.html',
  styleUrls: ['export-reports.component.scss'],
})
export class ExportReportsComponent implements OnInit {
  message: string;
  currentYear = new Date().getFullYear();
  leaveBalanceYearFormControl = new FormControl(this.currentYear, [Validators.required, Validators.min(1900), Validators.max(2500)]);

  invalidYearAndMonth = false;
  invalidYearAndMonthMessage = '';

  departments: IDepartment[] = [];
  designations: IDesignation[] = [];
  units: IUnit[] = [];

  years = [
    this.currentYear + 1,
    this.currentYear,
    this.currentYear - 1,
    this.currentYear - 2,
    this.currentYear - 3,
    this.currentYear - 4,
    this.currentYear - 5,
    this.currentYear - 6,
    this.currentYear - 7,
  ];

  months = [
    { value: 1, text: 'January' },
    { value: 2, text: 'February' },
    { value: 3, text: 'March' },
    { value: 4, text: 'April' },
    { value: 5, text: 'May' },
    { value: 6, text: 'June' },
    { value: 7, text: 'July' },
    { value: 8, text: 'August' },
    { value: 9, text: 'September' },
    { value: 10, text: 'October' },
    { value: 11, text: 'November' },
    { value: 12, text: 'December' },
  ];

  taxReturnForm = this.fb.group({
    startYear: [null, [Validators.required]],
    startMonth: [null, [Validators.required]],
    endYear: [null, [Validators.required]],
    endMonth: [null, [Validators.required]],
  });

  employeeDocReportForm = this.fb.group({
    year: [null, [Validators.required]],
    month: [null, [Validators.required]],
  });

  monthlyAttendanceFilterForm = this.fb.group({
    searchText: [''],
    departmentId: [0, [Validators.required]],
    designationId: [0, [Validators.required]],
    unitId: [0, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
  });

  constructor(
    protected exportReportsService: ExportReportsService,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected departmentService: DepartmentService,
    protected designationService: DesignationService,
    protected unitService: UnitService
  ) {
    this.message = 'ExportReportsComponent message';
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loadDepartments();
    this.loadDesignations();
    this.loadUnits();
  }

  loadDepartments(): void {
    this.departmentService.query().subscribe(res => {
      this.departments = res.body || [];
    });
  }

  loadDesignations(): void {
    this.designationService.query().subscribe(res => {
      this.designations = res.body || [];
    });
  }

  loadUnits(): void {
    this.unitService.query().subscribe(res => {
      this.units = res.body || [];
    });
  }

  export(): void {
    const fileName = 'EmployeeReports.xlsx';
    this.exportReportsService.exportEmployeeXlsx().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
  }

  exportLeaveBalance(): void {
    const year = this.leaveBalanceYearFormControl.value;
    if (!year || year < 1900 || year > 2500) {
      swalForWarningWithMessage('Year is not valid!', 5000);
      return;
    }
    const fileName = `LeaveBalance-${year}.xlsx`;
    swalOnLoading('Downloading');
    this.exportReportsService.exportLeaveBalanceXlsx(year).subscribe(
      x => {
        swalClose();
        const newBlob = new Blob([x], { type: 'application/octet-stream' });

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
          (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
          return;
        }
        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = fileName;
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        // tslint:disable-next-line:typedef
        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      },
      errObj => {
        swalOnRequestError();
      }
    );
  }

  exportEmployeeTin(): void {
    const fileName = 'Employee_TIN_Number.csv';
    this.exportReportsService.exportEmployeeTinXlsx().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
  }

  exportHolidays(): void {
    swalOnLoading('Loading ...');
    const fileName = 'Holidays.xlsx';
    this.exportReportsService.exportHolidays().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
    swalClose();
  }

  exportInsuranceRegistrations(): void {
    swalOnLoading('Loading ...');
    const fileName = 'InsuranceRegistrations.xlsx';
    this.exportReportsService.exportInsuranceRegistrations().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
    swalClose();
  }

  exportApprovedInsuranceRegistrations(): void {
    swalOnLoading('Loading ...');
    const fileName = 'ApprovedInsuranceRegistrations.xlsx';
    this.exportReportsService.exportApprovedInsuranceRegistrations().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
    swalClose();
  }

  exportInsuranceClaim(): void {
    swalOnLoading('Loading ...');
    const fileName = 'InsuranceClaims.xlsx';
    this.exportReportsService.exportInsuranceClaim().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
    swalClose();
  }

  exportEmployeePins(): void {
    swalOnLoading('Loading ...');
    const fileName = 'Employee_PINs.xlsx';
    this.exportReportsService.exportEmployeePins().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
    swalClose();
  }

  exportReferencePins(): void {
    swalOnLoading('Loading ...');
    const fileName = 'Employee_Reference_PINs.xlsx';
    this.exportReportsService.exportEmployeeReferencePins().subscribe(x => {
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }
      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
    swalClose();
  }

  openYearlyTaxReturnSubmissionModal(content: any): void {
    this.modalService.open(content).result.then(
      result => {
        this.exportYearlyTaxReturnSubmissionModal();
      },
      reason => {
        this.taxReturnForm.reset();
      }
    );
  }

  openDateRangeSalaryModal(content: any): void {
    this.modalService.open(content).result.then(
      result => {
        this.dateRangeSalaryModalSubmission();
      },
      reason => {
        this.taxReturnForm.reset();
      }
    );
  }

  openMonthlyAtsExportModal(content: any): void {
    this.loadData();
    const today = new Date();
    this.monthlyAttendanceFilterForm.get(['endDate'])!.setValue(dayjs(today));
    this.monthlyAttendanceFilterForm.get(['startDate'])!.setValue(dayjs(today).subtract(1, 'months'));
    this.monthlyAttendanceFilterForm.get(['searchText'])!.setValue('');
    this.monthlyAttendanceFilterForm.get(['departmentId'])!.setValue(0);
    this.monthlyAttendanceFilterForm.get(['designationId'])!.setValue(0);
    this.monthlyAttendanceFilterForm.get(['unitId'])!.setValue(0);

    this.modalService.open(content).result.then(
      result => {
        this.monthlyAtsExportModalSubmission();
      },
      reason => {
        this.monthlyAttendanceFilterForm.reset();
      }
    );
  }

  openEmployeeDocsReportExportModal(content: any): void {
    this.modalService.open(content).result.then(
      result => {
        this.employeeDocsReportExportModalSubmission();
      },
      reason => {
        this.employeeDocReportForm.reset();
      }
    );
  }

  employeeDocsReportExportModalSubmission(): void {
    const fileName = 'Monthly ATS Export.xlsx';
    const warningMessage = 'Are you sure you want to export Employee Doc Detailed Report?';

    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        swalOnLoading('Loading...');
        this.exportReportsService
          .exportEmployeeDocDetailedReport({
            year: this.employeeDocReportForm.get(['year'])!.value,
            month: this.employeeDocReportForm.get(['month'])!.value,
          })
          .subscribe(
            x => {
              const newBlob = new Blob([x], { type: 'application/octet-stream' });

              // IE doesn't allow using a blob object directly as link href
              // instead it is necessary to use msSaveOrOpenBlob
              if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
                (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
                return;
              }
              // For other browsers:
              // Create a link pointing to the ObjectURL containing the blob.
              const data = window.URL.createObjectURL(newBlob);

              const link = document.createElement('a');
              link.href = data;
              link.download = fileName;
              // this is necessary as link.click() does not work on the latest firefox
              link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

              // tslint:disable-next-line:typedef
              setTimeout(function () {
                // For Firefox it is necessary to delay revoking the ObjectURL
                window.URL.revokeObjectURL(data);
                link.remove();
              }, 100);

              swalClose();
              this.monthlyAttendanceFilterForm.reset();
            },
            err => {
              swalClose();
              swalOnRequestError();
              this.monthlyAttendanceFilterForm.reset();
            }
          );
      }
    });
  }

  private monthlyAtsExportModalSubmission(): void {
    const fileName = 'Monthly ATS Export.xlsx';
    const warningMessage = 'Are you sure you want to export Monthly ATS Export?';

    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        swalOnLoading('Loading...');
        this.exportReportsService
          .exportMonthlyAtsReport({
            searchText: this.monthlyAttendanceFilterForm.get(['searchText'])!.value ?? '',
            departmentId: this.monthlyAttendanceFilterForm.get(['departmentId'])!.value ?? 0,
            designationId: this.monthlyAttendanceFilterForm.get(['designationId'])!.value ?? 0,
            unitId: this.monthlyAttendanceFilterForm.get(['unitId'])!.value ?? 0,
            startDate: dayjs(this.monthlyAttendanceFilterForm.get(['startDate'])!.value).format(DATE_FORMAT),
            endDate: dayjs(this.monthlyAttendanceFilterForm.get(['endDate'])!.value).format(DATE_FORMAT),
          })
          .subscribe(
            x => {
              const newBlob = new Blob([x], { type: 'application/octet-stream' });

              // IE doesn't allow using a blob object directly as link href
              // instead it is necessary to use msSaveOrOpenBlob
              if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
                (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
                return;
              }
              // For other browsers:
              // Create a link pointing to the ObjectURL containing the blob.
              const data = window.URL.createObjectURL(newBlob);

              const link = document.createElement('a');
              link.href = data;
              link.download = fileName;
              // this is necessary as link.click() does not work on the latest firefox
              link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

              // tslint:disable-next-line:typedef
              setTimeout(function () {
                // For Firefox it is necessary to delay revoking the ObjectURL
                window.URL.revokeObjectURL(data);
                link.remove();
              }, 100);

              swalClose();
              this.monthlyAttendanceFilterForm.reset();
            },
            err => {
              swalClose();
              swalOnRequestError();
              this.monthlyAttendanceFilterForm.reset();
            }
          );
      }
    });
  }

  private exportYearlyTaxReturnSubmissionModal(): void {
    const fileName = 'Yearly Tax Return submission.xlsx';
    const warningMessage = 'Are You Sure?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        swalOnLoading('Loading...');
        this.exportReportsService
          .exportYearlyTaxReturnSubmissionReport({
            startYear: this.taxReturnForm.get(['startYear'])!.value,
            startMonth: this.taxReturnForm.get(['startMonth'])!.value,
            endYear: this.taxReturnForm.get(['endYear'])!.value,
            endMonth: this.taxReturnForm.get(['endMonth'])!.value,
          })
          .subscribe(x => {
            const newBlob = new Blob([x], { type: 'application/octet-stream' });

            // IE doesn't allow using a blob object directly as link href
            // instead it is necessary to use msSaveOrOpenBlob
            if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
              (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
              return;
            }
            // For other browsers:
            // Create a link pointing to the ObjectURL containing the blob.
            const data = window.URL.createObjectURL(newBlob);

            const link = document.createElement('a');
            link.href = data;
            link.download = fileName;
            // this is necessary as link.click() does not work on the latest firefox
            link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

            // tslint:disable-next-line:typedef
            setTimeout(function () {
              // For Firefox it is necessary to delay revoking the ObjectURL
              window.URL.revokeObjectURL(data);
              link.remove();
            }, 100);

            swalClose();
            this.taxReturnForm.reset();
          });
      }
    });
  }

  private dateRangeSalaryModalSubmission(): void {
    const fileName = 'Salary Export Within Date Range.xlsx';
    const warningMessage = 'Are You Sure?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        swalOnLoading('Loading...');
        this.exportReportsService
          .exportSalaryDateRange({
            startYear: this.taxReturnForm.get(['startYear'])!.value,
            startMonth: this.taxReturnForm.get(['startMonth'])!.value,
            endYear: this.taxReturnForm.get(['endYear'])!.value,
            endMonth: this.taxReturnForm.get(['endMonth'])!.value,
          })
          .subscribe(x => {
            const newBlob = new Blob([x], { type: 'application/octet-stream' });

            // IE doesn't allow using a blob object directly as link href
            // instead it is necessary to use msSaveOrOpenBlob
            if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
              (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
              return;
            }
            // For other browsers:
            // Create a link pointing to the ObjectURL containing the blob.
            const data = window.URL.createObjectURL(newBlob);

            const link = document.createElement('a');
            link.href = data;
            link.download = fileName;
            // this is necessary as link.click() does not work on the latest firefox
            link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

            // tslint:disable-next-line:typedef
            setTimeout(function () {
              // For Firefox it is necessary to delay revoking the ObjectURL
              window.URL.revokeObjectURL(data);
              link.remove();
            }, 100);

            swalClose();
            this.taxReturnForm.reset();
          });
      }
    });
  }

  onChangeYearAndMonth(): void {
    this.invalidYearAndMonth = false;
    this.invalidYearAndMonthMessage = '';

    const startYear = Number.parseInt(this.taxReturnForm.get(['startYear'])!.value, 10);
    const startMonth = Number.parseInt(this.taxReturnForm.get(['startMonth'])!.value, 10);
    const endYear = Number.parseInt(this.taxReturnForm.get(['endYear'])!.value, 10);
    const endMonth = Number.parseInt(this.taxReturnForm.get(['endMonth'])!.value, 10);

    if (
      startYear !== null &&
      startYear !== undefined &&
      startMonth !== null &&
      startMonth !== undefined &&
      endYear !== null &&
      endYear !== undefined &&
      endMonth !== null &&
      endMonth !== undefined
    ) {
      if (startYear > endYear) {
        this.invalidYearAndMonth = true;
        this.invalidYearAndMonthMessage = 'Starting year cannot be larger than ending year.';
      } else if (startYear === endYear) {
        if (startMonth > endMonth) {
          this.invalidYearAndMonth = true;
          this.invalidYearAndMonthMessage = 'Starting month cannot be larger than ending month.';
        } else {
          this.invalidYearAndMonthMessage = '';
        }
      } else {
        this.invalidYearAndMonthMessage = '';
      }
    } else {
      this.invalidYearAndMonthMessage = '';
    }
  }
}
