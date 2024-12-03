import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { FormControl } from '@angular/forms';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_DISABLED,
  SWAL_DISABLED_CONFIRMATION,
  SWAL_ENABLED,
  SWAL_ENABLED_CONFIRMATION,
  SWAL_REJECTED_ICON,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../shared/constants/color.code.constant';
import {
  swalClose,
  swalOnApprovalConfirmation,
  swalOnApprovedSuccess,
  swalOnLoading,
  swalOnRequestError,
} from '../../../shared/swal-common/swal-common';
import { IIncomeTaxDropDownMenu } from '../../../shared/model/drop-down-income-tax.model';
import { IncomeTaxStatementService } from '../../../payroll-management-system/income-tax-statement/income-tax-statement.service';
import {
  ITaxAcknowledgementReceipt
} from "../../../payroll-management-system/user-tax-acknowledgement-receipt/tax-acknowledgement-receipt.model";
import {TaxAcknowledgementReceiptService} from "../service/tax-acknowledgement-receipt.service";
import {UserTaxAcknowledgementReceiptService} from "../service/user-tax-acknowledgement-receipt.service";
import {ParseLinks} from "../../../core/util/parse-links.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {DataUtils} from "../../../core/util/data-util.service";
import {AcknowledgementStatus} from "../../enumerations/acknowledgement-status.model";

@Component({
  selector: 'jhi-tax-acknowledgement-receipt',
  templateUrl: './tax-acknowledgement-receipt.component.html',
})
export class TaxAcknowledgementReceiptComponent implements OnInit, OnDestroy {
  taxAcknowledgementReceipts: ITaxAcknowledgementReceipt[];
  aitConfigsYear!: IIncomeTaxDropDownMenu[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  totalPendingRequests!: number;
  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchText = new FormControl('');

  isEnabling = false;

  searchForm = this.fb.group({
    employeeId: [null],
    searchText: [''],
    fiscalYearId: [null],
  });

  constructor(
    protected taxAcknowledgementReceiptService: TaxAcknowledgementReceiptService,
    protected userTaxAcknowledgementReceiptService: UserTaxAcknowledgementReceiptService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private fb: FormBuilder,
    private incomeTaxStatementService: IncomeTaxStatementService
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
      .getReceivedTaxAcknowledgementReceipts({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        aitConfigId: this.searchForm.get('fiscalYearId')!.value,
        employeeId: this.searchForm.get('employeeId')!.value,
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
    this.incomeTaxStatementService.getAllAitConfigYears().subscribe(data => (this.aitConfigsYear = data.body!));
    this.loadAll();
    this.registerChangeInTaxAcknowledgementReceipts();
    this.taxAcknowledgementReceiptService.getTotalPendingTaxReceipts().subscribe(res => {
      this.totalPendingRequests = res;
    });
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

  approveSelected(): void {
    swalOnApprovalConfirmation().then(result => {
      if (result.isConfirmed) {
        this.isEnabling = true;
        this.subscribeToSaveResponse(this.taxAcknowledgementReceiptService.enableSelectedTax(this.approvalDTO));
      }
    });
  }

  clearAllChecks(): void {
    this.allSelector = false;
    this.approvalDTO.listOfIds = [];
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<boolean>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  private onSaveSuccess(): void {
    if (this.isEnabling) {
      swalOnApprovedSuccess();
    } else {
      swalOnRequestError();
    }
    this.clearAllChecks();
    this.loadAllAfterApproval();
  }

  loadAllAfterApproval(): void {}

  private onSaveError(): void {
    swalOnRequestError();
  }

  onKeydown(event: any): void {
    if (event.key === 'Backspace') {
      this.searchForm.get('employeeId')!.setValue(null);
      this.reset();
    }
  }

  onSearchTextChange(employee: any): void {
    this.searchForm.get(['employeeId'])!.setValue(employee.id!);
    this.reset();
  }

  changeFiscalYear(): void {
    this.reset();
  }

  getUserFriendlyUi(taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): string {
    if (taxAcknowledgementReceipt.acknowledgementStatus === AcknowledgementStatus.SUBMITTED) {
      return 'Submitted';
    } else if (taxAcknowledgementReceipt.acknowledgementStatus === AcknowledgementStatus.RECEIVED) {
      return 'Received';
    } else {
      return '';
    }
  }

  taxAcknowledgementExportDownload(): void {
    const fileName = 'TaxAcknowledgementReceipt.xlsx';
    const year = this.searchForm.get(['fiscalYearId'])!.value;

    if (year !== undefined && year !== null) {
      this.taxAcknowledgementReceiptService.taxAcknowledgementReceiptExportDownload(year).subscribe(x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
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
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: 'Please Select Fiscal Year',
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    }
  }

  export(id: number): void {
    swalOnLoading('Preparing for download...');
    this.taxAcknowledgementReceiptService.downloadTaxAcknowledgement(id).subscribe(
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
}
