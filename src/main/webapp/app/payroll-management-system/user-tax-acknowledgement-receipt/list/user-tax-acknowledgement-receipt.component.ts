import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { UserTaxAcknowledgementReceiptService } from '../service/user-tax-acknowledgement-receipt.service';
import { UserTaxAcknowledgementReceiptDetailModalComponent } from '../detail/user-tax-acknowledgement-receipt-detail-modal.component';
import { AcknowledgementStatus } from '../../../shared/model/enumerations/acknowledgement-status.model';

import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DELETE_CONFIRMATION,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../shared/constants/color.code.constant';
import { swalClose, swalOnDeleteSuccess, swalOnLoading, swalOnRequestError } from '../../../shared/swal-common/swal-common';
import {ITaxAcknowledgementReceipt, NewTaxAcknowledgementReceipt} from "../tax-acknowledgement-receipt.model";
import {DataUtils} from "../../../core/util/data-util.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {ParseLinks} from "../../../core/util/parse-links.service";

@Component({
  selector: 'jhi-tax-acknowledgement-receipt',
  templateUrl: './user-tax-acknowledgement-receipt.component.html',
})
export class UserTaxAcknowledgementReceiptComponent implements OnInit, OnDestroy {
  taxAcknowledgementReceipts: ITaxAcknowledgementReceipt[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected taxAcknowledgementReceiptService: UserTaxAcknowledgementReceiptService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.taxAcknowledgementReceipts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.taxAcknowledgementReceiptService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ITaxAcknowledgementReceipt[]>) => this.paginateTaxAcknowledgementReceipts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.taxAcknowledgementReceipts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTaxAcknowledgementReceipts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITaxAcknowledgementReceipt): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInTaxAcknowledgementReceipts(): void {
    this.eventSubscriber = this.eventManager.subscribe('taxAcknowledgementReceiptListModification', () => this.reset());
  }

  delete(taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.taxAcknowledgementReceiptService.delete(taxAcknowledgementReceipt.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.reset();
          },
          () => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTaxAcknowledgementReceipts(data: ITaxAcknowledgementReceipt[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.taxAcknowledgementReceipts.push(data[i]);
      }
    }
  }

  openTacAcknowledgementReceiptModal(taxAcknowledgementReceipt: any): void {
    const modalRef = this.modalService.open(UserTaxAcknowledgementReceiptDetailModalComponent, {
      size: 'lg',
      backdrop: true,
      keyboard: false,
    });
    modalRef.componentInstance.taxAcknowledgementReceipt = taxAcknowledgementReceipt;
  }

  getUserFriendlyUi(acknowledgementStatus: any): string {
    if (acknowledgementStatus === AcknowledgementStatus.SUBMITTED) {
      return 'Submitted';
    } else if (acknowledgementStatus === AcknowledgementStatus.RECEIVED) {
      return 'Received';
    } else {
      return '';
    }
  }

  private getFileName(disposition: string): string {
    const utf8FilenameRegex = /filename\*=UTF-8''([\w%\-\\.]+)(?:; ?|$)/i;
    const asciiFilenameRegex = /^filename=(["']?)(.*?[^\\])\1(?:; ?|$)/i;

    let fileName = '';
    if (utf8FilenameRegex.test(disposition)) {
      fileName = decodeURIComponent(utf8FilenameRegex.exec(disposition)![1]);
    } else {
      // prevent ReDos attacks by anchoring the ascii regex to string start and
      //  slicing off everything before 'filename='
      const filenameStart = disposition.toLowerCase().indexOf('filename=');
      if (filenameStart >= 0) {
        const partialDisposition = disposition.slice(filenameStart);
        const matches = asciiFilenameRegex.exec(partialDisposition);
        if (matches != null && matches[2]) {
          fileName = matches[2];
        }
      }
    }
    return fileName;
  }

  exportReport(id: number): void {
    swalOnLoading('Preparing for download...');
    this.taxAcknowledgementReceiptService.downloadTaxAcknowledgementReport(id).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should

        const fileName = this.getFileName(x.headers.get('content-disposition')!);
        const newBlob = new Blob([x.body!], { type: 'application/octet-stream' });

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
      err => {},
      () => {
        swalClose();
      }
    );
  }
}
