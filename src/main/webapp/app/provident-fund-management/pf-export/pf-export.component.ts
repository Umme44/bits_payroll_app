import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { PfExportService } from 'app/provident-fund-management/pf-export/pf-export.service';
import { swalClose, swalOnBatchDeleteConfirmation, swalOnLoading, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from '../../config/input.constants';

@Component({
  selector: 'jhi-pf-export',
  templateUrl: './pf-export.component.html',
})
export class PfExportComponent implements OnInit {
  selectedYearForOverallPfAmountReport: string | null | undefined = null;
  isSelectedYearInvalid = false;
  years: number[] = [];
  currentYear = new Date().getFullYear();

  invalidYearAndMonth = false;
  invalidYearAndMonthMessage = '';

  PFyears = [
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
  today: dayjs.Dayjs | undefined;

  pfStatementReportDateForm = this.fb.group({
    pfStatementReportDate: [dayjs().startOf('day'), [Validators.required]],
  });

  PFReturnForm = this.fb.group({
    startYear: [null, [Validators.required]],
    startMonth: [null, [Validators.required]],
    endYear: [null, [Validators.required]],
    endMonth: [null, [Validators.required]],
  });

  constructor(private pfExportService: PfExportService, protected fb: FormBuilder, protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.loadYears();
  }

  // Load years based on available pf collection in the system

  loadYears(): void {
    this.pfExportService.loadAllYears().subscribe(res => (this.years = res.body!));
  }

  exportMonthlyPfCollection(): void {
    swalOnLoading('loading..');
    const fileName = 'Monthly_PF_Collection.csv';

    this.pfExportService.monthlyPfCollectionExport().subscribe(
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
      },
      error => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  exportPfCollectionInterest(): void {
    swalOnLoading('loading..');
    const fileName = 'PF_Collection_Interest.csv';

    this.pfExportService.pfCollectionInterestsExport().subscribe(
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
      },
      error => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  exportPfOpeningBalance(): void {
    swalOnLoading('loading..');
    const fileName = 'PF_Collection_Opening_Balance.csv';

    this.pfExportService.pfOpeningBalanceExport().subscribe(
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
      },
      error => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  onChangeYear(event: any): void {
    this.isSelectedYearInvalid = false;
    const year = event.target.value;

    if (year !== null && year !== undefined && year !== '') {
      this.selectedYearForOverallPfAmountReport = year;
    } else {
      this.selectedYearForOverallPfAmountReport = null;
    }
  }

  exportAnnualPfReport(): void {
    const year = Number.parseInt(this.selectedYearForOverallPfAmountReport!, 10);

    swalOnLoading('loading..');
    const fileName = 'PF_annual_report.xlsx';

    this.pfExportService.annualPfAmountReport(year).subscribe(
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
      },
      error => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  exportPfContributionDetailedCsvReport(): void {
    swalOnLoading('loading..');
    const fileName = 'Detailed_PF_Contribution_Report.csv';

    this.pfExportService.detailedPfContributionReport().subscribe(
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
      },
      error => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  exportPfInterestDetailedCsvReport(): void {
    swalOnLoading('loading..');
    const fileName = 'Detailed_PF_Interest_Report.csv';

    this.pfExportService.detailedPfInterestReport().subscribe(
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
      },
      error => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  dateRangePF(content: any): void {
    this.modalService.open(content).result.then(
      result => {
        this.dateRangePFmodal();
      },
      reason => {
        this.PFReturnForm.reset();
      }
    );
  }

  private dateRangePFmodal(): void {
    const fileName = 'pf-collection-report.xlsx';
    const warningMessage = 'Are You Sure?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        swalOnLoading('Loading...');
        this.pfExportService
          .dateRangePfAmountReport({
            startYear: this.PFReturnForm.get(['startYear'])!.value,
            startMonth: this.PFReturnForm.get(['startMonth'])!.value,
            endYear: this.PFReturnForm.get(['endYear'])!.value,
            endMonth: this.PFReturnForm.get(['endMonth'])!.value,
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
            this.PFReturnForm.reset();
          });
      }
    });
  }

  onChangeYearAndMonth(): void {
    this.invalidYearAndMonth = false;
    this.invalidYearAndMonthMessage = '';

    const startYear = Number.parseInt(this.PFReturnForm.get(['startYear'])!.value, 10);
    const startMonth = Number.parseInt(this.PFReturnForm.get(['startMonth'])!.value, 10);
    const endYear = Number.parseInt(this.PFReturnForm.get(['endYear'])!.value, 10);
    const endMonth = Number.parseInt(this.PFReturnForm.get(['endMonth'])!.value, 10);

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
        }
      }
    }
  }

  exportPfStatementReport(content: any): void {
    this.today = dayjs().startOf('day');
    this.pfStatementReportDateForm.get(['pfStatementReportDate'])!.setValue(this.today);
    this.modalService.open(content).result.then(
      result => {
        this.exportPfStatementReportModal();
      },
      reason => {
        this.pfStatementReportDateForm.reset();
      }
    );
  }

  exportPfStatementReportModal(): void {
    swalOnLoading('loading..');
    const fileName = `${this.pfStatementReportDateForm.get(['pfStatementReportDate'])!.value.format(DATE_FORMAT)}-pf-statement.xlsx`;

    this.pfExportService.pfStatementReport(this.pfStatementReportDateForm.get(['pfStatementReportDate'])!.value).subscribe(x => {
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
}
