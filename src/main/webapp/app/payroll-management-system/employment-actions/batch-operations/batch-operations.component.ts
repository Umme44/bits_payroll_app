import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BatchOperationsService } from './batch-operations.service';
import { HttpResponse } from '@angular/common/http';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-import-data',
  templateUrl: './batch-operations.component.html',
  styleUrls: ['batch-operations.component.scss'],
})
export class BatchOperationsComponent implements OnInit {
  uploadReady: boolean;

  workFromHomeApplicationUploadReady: boolean = false;
  workFromHomeApplicationFileToUpload: any;
  batchWorkFromHomeApplicationImportBtnTxt: String = 'Import';
  batchWorkFromHomeApplicationImportSuccess: boolean | null = false;
  errorOnBatchWorkFromHomeApplicationImport: boolean | null = false;

  batchPromotionsImportSuccess: boolean | null = false;
  batchPromotionsImportBtnTxt: String = 'Promote';
  errorOnBatchPromotionsImport: boolean | null = false;

  incrementImportSuccess: boolean | null = false;
  incrementImportBtnTxt: String = 'Increment';
  errorOnIncrementImport: boolean | null = false;

  transferImportSuccess: boolean | null = false;
  transferImportBtnTxt: String = 'Transfer';
  errorOnTransferImport: boolean | null = false;

  arrearImportSuccess: boolean | null = false;
  arrearImportBtnTxt: String = 'Arrear';
  errorOnArrearImport: boolean | null = false;

  dduImportSuccess: boolean | null = false;
  dduImportBtnTxt: String = 'Update';
  errorOnDduImport: boolean | null = false;

  garbageAttendanceImportSuccess: boolean | null = false;
  garbageAttendanceImportBtnTxt: String = 'Update';
  errorOnGarbageAttendanceImport: boolean | null = false;

  flexScheduleImportSuccess: boolean | null = false;
  flexScheduleImportBtnTxt: String = 'Update';
  errorOnFlexScheduleImport: boolean | null = false;

  festivalBonusImportSuccess: boolean | null = false;
  festivalBonusImportBtnTxt: String = 'Import';
  errorOnFestivalBonusImport: boolean | null = false;

  taxChallanImportSuccess: boolean | null = false;
  taxChallanImportBtnTxt: String = 'Import';
  errorOnTaxChallanImport: boolean | null = false;

  employeeDocumentsZipFileToUpload: any;
  employeeDocumentsUploadReady: boolean;
  employeeDocumentImportSuccess: boolean | null = false;
  employeeDocumentImportTxt: String = 'Upload';
  errorOnEmployeeDocumentImport: boolean | null = false;

  employeeDocumentsXlsxFileToUpload: any;
  employeeDocumentsXlsxUploadReady: boolean;
  employeeDocumentXlsxImportSuccess: boolean | null = false;
  employeeDocumentXlsxImportTxt: String = 'Upload';
  errorOnEmployeeDocumentXlsxImport: boolean | null = false;

  billableAugmentedDataImportSuccess: boolean | null = false;
  billableAugmentedDataImportBtnTxt: String = 'Import';
  errorOnBillableAugmentedDataImport: boolean | null = false;
  billableAugmentedDataFileToUpload: any;
  billableAugmentedDataImportUploadBtn: boolean | null = false;
  billableAugmentedDataImportUploadReady: boolean | null = false;

  private fileToUpload: any;

  constructor(protected batchOperationsService: BatchOperationsService, protected modalService: NgbModal) {
    this.uploadReady = false;
    this.employeeDocumentsUploadReady = false;
    this.employeeDocumentsXlsxUploadReady = false;
    this.billableAugmentedDataImportUploadReady = false;
  }

  ngOnInit(): void {}

  handleFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.fileToUpload = ev.target.files[0];
      this.uploadReady = true;
    } else {
      this.uploadReady = false;
    }
  }
  handleEmployeeDocumentsFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.employeeDocumentsZipFileToUpload = ev.target.files[0];
      this.employeeDocumentsUploadReady = true;
    } else {
      this.employeeDocumentsUploadReady = false;
    }
  }
  handleEmployeeDocumentsExcelFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.employeeDocumentsXlsxFileToUpload = ev.target.files[0];
      this.employeeDocumentsXlsxUploadReady = true;
    } else {
      this.employeeDocumentsXlsxUploadReady = false;
    }
  }

  handleWorkFromHomeApplicationFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.workFromHomeApplicationFileToUpload = ev.target.files[0];
      this.workFromHomeApplicationUploadReady = true;
    } else {
      this.workFromHomeApplicationUploadReady = false;
    }
  }

  handleBillableAugmentedDataXlsxFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.billableAugmentedDataFileToUpload = ev.target.files[0];
      this.billableAugmentedDataImportUploadReady = true;
    } else {
      this.billableAugmentedDataImportUploadReady = false;
    }
  }

  importWorkFromHomeApplication(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.workFromHomeApplicationXlsxFile(this.workFromHomeApplicationFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.batchWorkFromHomeApplicationImportSuccess = resp.body;
        this.errorOnBatchWorkFromHomeApplicationImport = !this.batchWorkFromHomeApplicationImportSuccess;
        this.batchWorkFromHomeApplicationImportBtnTxt = 'Re Import';

        if (this.batchWorkFromHomeApplicationImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  importXlsx(type: string): void {
    switch (type) {
      case 'billable-augmented-api':
        this.billableAugmentedBatchUpdate();
        break;
      case 'batch_promotion':
        this.batchPromotions();
        break;
      case 'work_from_home':
        this.importWorkFromHomeApplication();
        break;
      case 'batch_increment':
        this.batchIncrements();
        break;
      case 'batch_transfer':
        this.batchTransfers();
        break;
      case 'batch_arrear':
        this.batchArrear();
        break;
      case 'tax_challan':
        this.taxChallan();
        break;
      case 'batch_ddu':
        this.batchDdu();
        break;
      case 'garbage-atten':
        this.garbageAtten();
        break;
      case 'flex-schedule':
        this.flexSchedule();
        break;
      case 'festival-bonus':
        this.festivalBonus();
        break;
    }
  }

  private garbageAtten(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.garbageAttenXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.garbageAttendanceImportSuccess = resp.body;
        this.errorOnGarbageAttendanceImport = !this.garbageAttendanceImportSuccess;
        this.garbageAttendanceImportBtnTxt = 'Re Import';

        if (this.garbageAttendanceImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private festivalBonus(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.festivalBonusXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.festivalBonusImportSuccess = resp.body;
        this.errorOnFestivalBonusImport = !this.festivalBonusImportSuccess;
        this.festivalBonusImportBtnTxt = 'Re Import';

        if (this.festivalBonusImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private flexSchedule(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.flexScheduleXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.flexScheduleImportSuccess = resp.body;
        this.errorOnFlexScheduleImport = !this.flexScheduleImportSuccess;
        this.flexScheduleImportBtnTxt = 'Re Import';

        if (this.flexScheduleImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private batchDdu(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.batchDDUXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.dduImportSuccess = resp.body;
        this.errorOnDduImport = !this.dduImportSuccess;
        this.dduImportBtnTxt = 'Re Import';

        if (this.dduImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private batchPromotions(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.batchPromotionsXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.batchPromotionsImportSuccess = resp.body;
        this.errorOnBatchPromotionsImport = !this.batchPromotionsImportSuccess;
        this.batchPromotionsImportBtnTxt = 'Re Import';

        if (this.batchPromotionsImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private batchIncrements(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.batchIncrementXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.incrementImportSuccess = resp.body;
        this.errorOnIncrementImport = !this.incrementImportSuccess;
        this.incrementImportBtnTxt = 'Re Import';

        if (this.incrementImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private batchTransfers(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.batchTransferXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.transferImportSuccess = resp.body;
        this.errorOnTransferImport = !this.transferImportSuccess;
        this.transferImportBtnTxt = 'Re Import';

        if (this.transferImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private batchArrear(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.batchArrearXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.arrearImportSuccess = resp.body;
        this.errorOnArrearImport = !this.arrearImportSuccess;
        this.arrearImportBtnTxt = 'Re Import';

        if (this.arrearImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private taxChallan(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.taxChallanXlsxFile(this.fileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.taxChallanImportSuccess = resp.body;
        this.errorOnTaxChallanImport = !this.taxChallanImportSuccess;
        this.taxChallanImportBtnTxt = 'Re Import';

        if (this.taxChallanImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
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

  showErrorModal(): void {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Something went wrong!',
      footer: '<a>Failed to process your request!</a>',
    });
  }

  importEmployeeDocument(content: any): void {
    this.modalService.open(content, { size: 'md', centered: true }).result.then(
      result => {},
      reason => {}
    );
  }

  cancel(): void {
    this.employeeDocumentsZipFileToUpload = false;
    this.employeeDocumentsXlsxFileToUpload = false;
    this.modalService.dismissAll();
  }

  import(): void {
    // this.importEmployeeDocumentsZip()
    this.batchOperationsService
      .importEmployeeDocuments(this.employeeDocumentsZipFileToUpload, this.employeeDocumentsXlsxFileToUpload)
      .subscribe(
        (resp: HttpResponse<boolean>) => {
          this.cancel();
          Swal.close();
          this.employeeDocumentImportSuccess = resp.body;
          this.errorOnEmployeeDocumentImport = !this.employeeDocumentImportSuccess;
          this.employeeDocumentImportTxt = 'Re Import';

          if (this.employeeDocumentImportSuccess === false) {
            this.showBadDataFormatModal();
          } else {
            this.showFileProcessSuccesModal();
          }
        },
        error => {
          this.cancel();
          this.showErrorModal();
        }
      );
  }

  disableEmployeeDocumentUploadBtn(): boolean {
    if (this.employeeDocumentsUploadReady && this.employeeDocumentsXlsxUploadReady) {
      return false;
    } else return true;
  }

  dismiss(): void {
    this.modalService.dismissAll();
  }

  billableAugmentedBatchUpdate(): void {
    this.startFileUploadingOnProcessModal();
    this.batchOperationsService.billableAugmentedDataXlsxFile(this.billableAugmentedDataFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.batchPromotionsImportSuccess = resp.body;
        this.errorOnBatchPromotionsImport = !this.batchPromotionsImportSuccess;
        this.batchPromotionsImportBtnTxt = 'Re Import';

        if (this.batchPromotionsImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccesModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }
}
