import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { FormBuilder, FormControl } from '@angular/forms';
import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../shared/constants/color.code.constant';
import { swalClose, swalOnLoading, swalOnRequestError } from '../../../shared/swal-common/swal-common';
import { IIncomeTaxDropDownMenu } from '../../../shared/model/drop-down-income-tax.model';
import { IncomeTaxStatementService } from '../../../payroll-management-system/income-tax-statement/income-tax-statement.service';
import {
  ITaxAcknowledgementReceipt
} from "../../../payroll-management-system/user-tax-acknowledgement-receipt/tax-acknowledgement-receipt.model";
import {TaxAcknowledgementReceiptService} from "../service/tax-acknowledgement-receipt.service";
import {DataUtils} from "../../../core/util/data-util.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {ParseLinks} from "../../../core/util/parse-links.service";
import {AcknowledgementStatus} from "../../enumerations/acknowledgement-status.model";

@Component({
  selector: 'jhi-tax-acknowledgement-receipt',
  templateUrl: './tax-acknowledgement-receipt-finance-approvals.component.html',
})
export class TaxAcknowledgementReceiptFinanceApprovalsComponent implements OnInit, OnDestroy {
  taxAcknowledgementReceipts: ITaxAcknowledgementReceipt[] = [];
  aitConfigsYear: IIncomeTaxDropDownMenu[] = [];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  selectedEmployeeId!: number;

  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchText = new FormControl('');

  isSelectedByToggle = false;

  isEnabling = false;

  searchForm = this.fb.group({
    searchText: [''],
  });

  constructor(
    protected taxAcknowledgementReceiptFinanceService: TaxAcknowledgementReceiptService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private fb: FormBuilder,
    private incomeTaxStatementService: IncomeTaxStatementService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.taxAcknowledgementReceiptFinanceService
      .getSubmittedTaxAcknowledgementReceipts({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ITaxAcknowledgementReceipt[]>) => {
        this.paginateTaxAcknowledgementReceipts(res.body, res.headers);
        this.taxAcknowledgementReceipts.filter(s => (s.isChecked = false));
      });
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
    this.incomeTaxStatementService.getAllAitConfigYears().subscribe(data => (this.aitConfigsYear = data.body!));
    this.registerChangeInTaxAcknowledgementReceipts();
    this.approvalDTO.listOfIds = [];
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
    Swal.fire({
      text: 'Receive?',
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.isEnabling = true;
        this.subscribeToSaveResponse(this.taxAcknowledgementReceiptFinanceService.enableSelectedTax(this.approvalDTO));
      } else {
        this.approvalDTO.listOfIds = [];
        if (this.isSelectedByToggle) {
          this.approvalDTO.listOfIds = [];
        }
        this.isSelectedByToggle = false;
      }
    });
  }

  /*rejectSelected(): void {
    Swal.fire({
      text: SWAL_DISABLED_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.isEnabling = false;
        this.subscribeToSaveResponse(this.taxAcknowledgementReceiptService.disableSelectedLM(this.approvalDTO));

      }
    });
  }*/

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
      Swal.fire({
        icon: SWAL_APPROVED_ICON,
        text: 'Received',
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: SWAL_REJECTED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    }
    this.clearAllChecks();
    this.loadAllAfterApproval();
  }

  loadAllAfterApproval(): void {
    if (this.selectedEmployeeId === undefined) {
      this.taxAcknowledgementReceipts = [];
      this.taxAcknowledgementReceiptFinanceService
        .getSubmittedTaxAcknowledgementReceipts({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<ITaxAcknowledgementReceipt[]>) => {
          this.paginateTaxAcknowledgementReceipts(res.body, res.headers);
          this.taxAcknowledgementReceipts.filter(s => (s.isChecked = false));
        });
    } else {
      this.taxAcknowledgementReceipts = [];
      this.taxAcknowledgementReceiptFinanceService
        .getSubmittedTaxAcknowledgementReceipts({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
          employeeId: this.selectedEmployeeId,
        })
        .subscribe((res: HttpResponse<ITaxAcknowledgementReceipt[]>) => {
          this.paginateTaxAcknowledgementReceipts(res.body, res.headers);
          this.taxAcknowledgementReceipts.filter(s => (s.isChecked = false));
        });
    }
  }

  private onSaveError(): void {
    swalOnRequestError();
  }

  onKeydown(event: any): void {
    if (event.key === 'Backspace') {
      this.taxAcknowledgementReceipts = [];
      this.loadAll();
    }
  }

  onSearchTextChangeV2(employee: any): void {
    this.selectedEmployeeId = employee.id!;
    this.taxAcknowledgementReceipts = [];
    this.taxAcknowledgementReceiptFinanceService
      .getSubmittedTaxAcknowledgementReceipts({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.selectedEmployeeId,
      })
      .subscribe((res: HttpResponse<ITaxAcknowledgementReceipt[]>) => {
        this.paginateTaxAcknowledgementReceipts(res.body, res.headers);
        this.taxAcknowledgementReceipts.filter(s => (s.isChecked = false));
      });
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

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.taxAcknowledgementReceipts !== undefined) {
      this.taxAcknowledgementReceipts = this.taxAcknowledgementReceipts.map(d => {
        if (d.id === id) {
          d.isChecked = isChecked;
          this.allSelector = false;
          return d;
        }
        if (id === -1) {
          d.isChecked = this.allSelector;
          return d;
        }
        return d;
      });
    }
    // clear previous set
    this.selectedIdSet.clear();
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    for (let i = 0; i < this.taxAcknowledgementReceipts!.length; i++) {
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
      if (this.taxAcknowledgementReceipts![i].isChecked === true) {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
        this.selectedIdSet.add(this.taxAcknowledgementReceipts![i].id);
      }
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
  }

  onReceived(id: number): void {
    this.selectedIdSet.clear();
    this.selectedIdSet.add(id);
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
    this.approveSelected();
  }

  export(id: number): void {
    swalOnLoading('Preparing for download...');
    this.taxAcknowledgementReceiptFinanceService.downloadTaxAcknowledgement(id).subscribe(
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
