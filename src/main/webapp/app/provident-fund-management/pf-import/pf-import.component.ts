import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { EventManager } from 'app/core/util/event-manager.service';
import { PfImportService } from './pf-import.service';
import { HttpResponse } from '@angular/common/http';
import Swal from 'sweetalert2';
import {
  swalClose,
  swalOnLoading,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSavedSuccess,
} from '../../shared/swal-common/swal-common';
import { swalConfirmationWithMessage } from '../../shared/swal-common/swal-confirmation.common';

@Component({
  selector: 'jhi-pf-import',
  templateUrl: './pf-import.component.html',
})
export class PfImportComponent implements OnInit, OnDestroy {
  eventSubscriber?: Subscription;

  pfAccountImportSuccess: boolean | null = false;
  pfAccountImportBtnTxt: String = 'Import';
  errorOnPfAccountImport: boolean | null = false;
  pfAccountUploadReady: boolean;

  // Previous employee pf account
  previousPfAccountImportSuccess: boolean | null = false;
  previousPfAccountImportBtnTxt: String = 'Import';
  errorOnPreviousPfAccountImport: boolean | null = false;
  previousPfAccountUploadReady: boolean;

  // pf collection
  pfCollectionImportSuccess: boolean | null = false;
  pfCollectionImportBtnTxt: String = 'Import';
  errorOnPfCollectionImport: boolean | null = false;
  pfCollectionUploadReady: boolean;

  // pf collection monthly
  pfCollectionMonthlyImportSuccess: boolean | null = false;
  pfCollectionMonthlyImportBtnTxt: String = 'Import';
  errorOnPfCollectionMonthlyImport: boolean | null = false;
  pfCollectionMonthlyUploadReady: boolean;

  //previous pf collection monthly
  previousPfCollectionMonthlyImportSuccess: boolean | null = false;
  previousPfCollectionMonthlyImportBtnTxt: String = 'Import';
  errorOnPreviousPfCollectionMonthlyImport: boolean | null = false;
  previousPfCollectionMonthlyUploadReady: boolean;

  // pf collection interests
  pfCollectionInterestsImportSuccess: boolean | null = false;
  pfCollectionInterestsImportBtnTxt: String = 'Import';
  errorOnPfCollectionInterestsImport: boolean | null = false;
  pfCollectionInterestUploadReady: boolean;

  // previous pf collection interests
  previousPfCollectionInterestsImportSuccess: boolean | null = false;
  previousPfCollectionInterestsImportBtnTxt: String = 'Import';
  errorOnPreviousPfCollectionInterestsImport: boolean | null = false;
  previousPfCollectionInterestUploadReady: boolean;

  // import gross and basic to pf collections
  grossAndBasicImportSuccess: boolean | null = false;
  grossAndBasicImportButtonText: String = 'Import';
  errorOnImportingGrossAndBasic: boolean | null = false;
  grossAndBasicUploadReady: boolean;

  // pf collection opening balance
  pfCollectionOpeningBalanceImportSuccess: boolean | null = false;
  pfCollectionOpeningBalanceImportBtnTxt: String = 'Import';
  errorOnPfCollectionOpeningBalanceImport: boolean | null = false;
  pfCollectionOpeningBalanceUploadReady: boolean;

  // previous pf collection opening balance
  previousPfCollectionOpeningBalanceImportSuccess: boolean | null = false;
  previousPfCollectionOpeningBalanceImportBtnTxt: String = 'Import';
  errorOnPreviousPfCollectionOpeningBalanceImport: boolean | null = false;
  previousPfCollectionOpeningBalanceUploadReady: boolean;

  // pf loan
  pfLoanImportSuccess: boolean | null = false;
  pfLoanImportBtnTxt: String = 'Import';
  errorOnPfLoanImport: boolean | null = false;
  pfLoanUploadReady: boolean;

  // private fileToUpload: any;

  private pfAccountFileToUpload: any;
  private previousPfAccountFileToUpload: any;
  private pfCollectionFileToUpload: any;
  private pfCollectionMonthlyFileToUpload: any;
  private previousPfCollectionMonthlyFileToUpload: any;
  private pfCollectionInterestFileToUpload: any;
  private previousPfCollectionInterestFileToUpload: any;
  private grossAndBasicFileToUpload: any;
  private pfCollectionOpeningBalanceFileToUpload: any;
  private previousPfCollectionOpeningBalanceFileToUpload: any;
  private pfLoanFileToUpload: any;

  constructor(protected pfImportService: PfImportService, protected configService: PfImportService, protected eventManager: EventManager) {
    this.pfAccountUploadReady = false;
    this.previousPfAccountUploadReady = false;
    this.pfCollectionUploadReady = false;
    this.pfCollectionMonthlyUploadReady = false;
    this.previousPfCollectionMonthlyUploadReady = false;
    this.pfCollectionInterestUploadReady = false;
    this.previousPfCollectionInterestUploadReady = false;
    this.grossAndBasicUploadReady = false;
    this.pfCollectionOpeningBalanceUploadReady = false;
    this.previousPfCollectionOpeningBalanceUploadReady = false;
    this.pfLoanUploadReady = false;
  }

  ngOnInit(): void {}

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  // handleFileInput(ev: any): void {
  //   if (ev.target.files != null && ev.target.files.length > 0) {
  //     this.fileToUpload = ev.target.files[0];
  //   }
  // }

  handlePfAccountFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.pfAccountFileToUpload = ev.target.files[0];
      this.pfAccountUploadReady = true;
    } else {
      this.pfAccountUploadReady = false;
    }
  }

  handlePreviousPfAccountFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.previousPfAccountFileToUpload = ev.target.files[0];
      this.previousPfAccountUploadReady = true;
    } else {
      this.previousPfAccountUploadReady = false;
    }
  }

  handlePfCollectionFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.pfCollectionFileToUpload = ev.target.files[0];
      this.pfCollectionUploadReady = true;
    } else {
      this.pfCollectionUploadReady = false;
    }
  }

  handlePfCollectionMonthlyFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.pfCollectionMonthlyFileToUpload = ev.target.files[0];
      this.pfCollectionMonthlyUploadReady = true;
    } else {
      this.pfCollectionMonthlyUploadReady = false;
    }
  }

  handlePreviousPfCollectionMonthlyFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.previousPfCollectionMonthlyFileToUpload = ev.target.files[0];
      this.previousPfCollectionMonthlyUploadReady = true;
    } else {
      this.previousPfCollectionMonthlyUploadReady = false;
    }
  }

  handlePfCollectionInterestFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.pfCollectionInterestFileToUpload = ev.target.files[0];
      this.pfCollectionInterestUploadReady = true;
    } else {
      this.pfCollectionInterestUploadReady = false;
    }
  }

  handlePreviousPfCollectionInterestFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.previousPfCollectionInterestFileToUpload = ev.target.files[0];
      this.previousPfCollectionInterestUploadReady = true;
    } else {
      this.previousPfCollectionInterestUploadReady = false;
    }
  }

  handleGrossAndBasicFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.grossAndBasicFileToUpload = ev.target.files[0];
      this.grossAndBasicUploadReady = true;
    } else {
      this.grossAndBasicUploadReady = false;
    }
  }

  handlePfOpeningBalanceFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.pfCollectionOpeningBalanceFileToUpload = ev.target.files[0];
      this.pfCollectionOpeningBalanceUploadReady = true;
    } else {
      this.pfCollectionOpeningBalanceUploadReady = false;
    }
  }

  handlePreviousPfOpeningBalanceFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.previousPfCollectionOpeningBalanceFileToUpload = ev.target.files[0];
      this.previousPfCollectionOpeningBalanceUploadReady = true;
    } else {
      this.previousPfCollectionOpeningBalanceUploadReady = false;
    }
  }

  handlePfLoanFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.pfLoanFileToUpload = ev.target.files[0];
      this.pfLoanUploadReady = true;
    } else {
      this.pfLoanUploadReady = false;
    }
  }

  importXlsx(type: string): void {
    switch (type) {
      case 'pf_account':
        this.pfAccountImport();
        break;
      case 'previous_pf_account':
        this.previousPfAccountImport();
        break;
      case 'pf_collection':
        this.pfCollectionImport();
        break;
      case 'pf_collection_opening_balance':
        this.pfCollectionOpeningBalanceImport();
        break;
      case 'previous_pf_collection_opening_balance':
        this.previousPfCollectionOpeningBalanceImport();
        break;
      case 'pf_collection_monthly_interest':
        this.pfCollectionInterestsImport();
        break;
      case 'previous_pf_collection_monthly_interest':
        this.previousPfCollectionInterestsImport();
        break;
      case 'pf_collection_monthly_collection':
        this.pfCollectionMonthlyImport();
        break;
      case 'previous_pf_collection_monthly_collection':
        this.previousPfCollectionMonthlyImport();
        break;
      case 'pf_loan_application':
        break;
      case 'pf_loan_Repayment':
        break;
      case 'pf_loan':
        this.pfLoanImport();
        break;
      case 'import-gross-and-basic':
        this.importGrossAndBasicToPfCollections();
        break;
      case 'pf_nominee':
        break;
    }
  }

  private pfAccountImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.pfAccountImport(this.pfAccountFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.pfAccountImportSuccess = resp.body;
        this.errorOnPfAccountImport = !this.pfAccountImportSuccess;
        this.pfAccountImportBtnTxt = 'Re Import';

        if (this.pfAccountImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private previousPfAccountImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.previousPfAccountImport(this.previousPfAccountFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.previousPfAccountImportSuccess = resp.body;
        this.errorOnPreviousPfAccountImport = !this.previousPfAccountImportSuccess;
        this.previousPfAccountImportBtnTxt = 'Re Import';

        if (this.previousPfAccountImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      err => {
        swalOnRequestErrorWithBackEndErrorTitle(err.error.title);
      }
    );
  }

  private pfCollectionImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.pfCollectionImport(this.pfCollectionFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.pfCollectionImportSuccess = resp.body;
        this.errorOnPfCollectionImport = !this.pfCollectionImportSuccess;
        this.pfCollectionImportBtnTxt = 'Re Import';

        if (this.pfCollectionImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private pfCollectionOpeningBalanceImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.pfCollectionOpeningBalanceImport(this.pfCollectionOpeningBalanceFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.pfCollectionOpeningBalanceImportSuccess = resp.body;
        this.errorOnPfCollectionOpeningBalanceImport = !this.pfCollectionOpeningBalanceImportSuccess;
        this.pfCollectionOpeningBalanceImportBtnTxt = 'Re Import';

        if (this.pfCollectionOpeningBalanceImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private previousPfCollectionOpeningBalanceImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.previousPfCollectionOpeningBalanceImport(this.previousPfCollectionOpeningBalanceFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.previousPfCollectionOpeningBalanceImportSuccess = resp.body;
        this.errorOnPreviousPfCollectionOpeningBalanceImport = !this.previousPfCollectionOpeningBalanceImportSuccess;
        this.previousPfCollectionOpeningBalanceImportBtnTxt = 'Re Import';

        if (this.previousPfCollectionOpeningBalanceImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private pfCollectionMonthlyImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.pfCollectionMonthlyImport(this.pfCollectionMonthlyFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.pfCollectionMonthlyImportSuccess = resp.body;
        this.errorOnPfCollectionMonthlyImport = !this.pfCollectionMonthlyImportSuccess;
        this.pfCollectionMonthlyImportBtnTxt = 'Re Import';

        if (this.pfCollectionMonthlyImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private previousPfCollectionMonthlyImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.previousPfCollectionMonthlyImport(this.previousPfCollectionMonthlyFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.previousPfCollectionMonthlyImportSuccess = resp.body;
        this.errorOnPreviousPfCollectionMonthlyImport = !this.previousPfCollectionMonthlyImportSuccess;
        this.previousPfCollectionMonthlyImportBtnTxt = 'Re Import';

        if (this.previousPfCollectionMonthlyImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private pfCollectionInterestsImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.pfCollectionInterestsImport(this.pfCollectionInterestFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.pfCollectionInterestsImportSuccess = resp.body;
        this.errorOnPfCollectionInterestsImport = !this.pfCollectionInterestsImportSuccess;
        this.pfCollectionInterestsImportBtnTxt = 'Re Import';

        if (this.pfCollectionInterestsImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private previousPfCollectionInterestsImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.previousPfCollectionInterestsImport(this.previousPfCollectionInterestFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.previousPfCollectionInterestsImportSuccess = resp.body;
        this.errorOnPreviousPfCollectionInterestsImport = !this.previousPfCollectionInterestsImportSuccess;
        this.previousPfCollectionInterestsImportBtnTxt = 'Re Import';

        if (this.previousPfCollectionInterestsImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  private pfLoanImport(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.pfLoanImport(this.pfLoanFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.pfLoanImportSuccess = resp.body;
        this.errorOnPfLoanImport = !this.pfLoanImportSuccess;
        this.pfLoanImportBtnTxt = 'Re Import';

        if (this.pfLoanImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
        }
      },
      error => {
        this.showErrorModal();
      }
    );
  }

  setGrossAndBasicToAllPfCollection(): void {
    swalConfirmationWithMessage(
      'Gross and Basic from employee salary will be added to PF Collection(From July 2021 to present). Do you want to proceed?'
    ).then(result => {
      if (result.isConfirmed) {
        swalOnLoading('Loading...');
        this.pfImportService.setGrossAndBasicToAllPfCollection().subscribe(res => {
          if (res.body === true) {
            swalClose();
            swalOnSavedSuccess();
          } else {
            swalClose();
            swalOnRequestError();
          }
        });
      }
    });
  }

  private importGrossAndBasicToPfCollections(): void {
    this.startFileUploadingOnProcessModal();
    this.pfImportService.importGrossAndBasicToPfCollections(this.grossAndBasicFileToUpload).subscribe(
      (resp: HttpResponse<boolean>) => {
        Swal.close();
        this.grossAndBasicImportSuccess = resp.body;
        this.errorOnImportingGrossAndBasic = !this.grossAndBasicImportSuccess;
        this.grossAndBasicImportButtonText = 'Re Import';

        if (this.grossAndBasicImportSuccess === false) {
          this.showBadDataFormatModal();
        } else {
          this.showFileProcessSuccessModal();
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

  showFileProcessSuccessModal(): void {
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
}
