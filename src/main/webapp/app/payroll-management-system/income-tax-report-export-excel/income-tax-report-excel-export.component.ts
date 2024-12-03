import { Component, OnInit } from '@angular/core';
import { IncomeTaxReportExcelExportService } from './income-tax-report-excel-export.service';
import { IIncomeTaxDropDownMenu } from '../../shared/model/drop-down-income-tax.model';
import { swalClose, swalOnLoading, swalOnRequestError } from '../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-income-tax-report-excel-export',
  templateUrl: './income-tax-report-excel-export.component.html',
  styleUrls: ['income-tax-report-excel-export.component.scss'],
})
export class IncomeTaxReportExcelExportComponent implements OnInit {
  dropDownMenus!: IIncomeTaxDropDownMenu[];
  selectedAitConfigId!: number;

  constructor(private incomeTaxReportExcelExportService: IncomeTaxReportExcelExportService) {}

  ngOnInit(): void {
    this.getAllYear();
  }

  getAllYear(): void {
    this.incomeTaxReportExcelExportService.getAllYear().subscribe(data => (this.dropDownMenus = data.body!));
  }

  exportIncomeTaxReportsInExcel(): void {
    const fileName = 'income_tax_report.xlsx';
    swalOnLoading('Loading...');

    this.incomeTaxReportExcelExportService.exportTaxReports(this.selectedAitConfigId).subscribe(
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
      err => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  onYearSelect(event: any): void {
    const aitConfigId = event.target.value;
    if (aitConfigId) {
      this.selectedAitConfigId = parseInt(aitConfigId, 10);
    }
  }
}
