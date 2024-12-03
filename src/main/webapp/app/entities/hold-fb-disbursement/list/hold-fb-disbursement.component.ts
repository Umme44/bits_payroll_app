import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';
import { HoldFbDisbursementService } from '../service/hold-fb-disbursement.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { HoldFbDisbursementDeleteDialogComponent } from '../delete/hold-fb-disbursement-delete-dialog.component';
@Component({
  selector: 'jhi-hold-fb-disbursement',
  templateUrl: './hold-fb-disbursement.component.html',
})
export class HoldFbDisbursementComponent implements OnInit, OnDestroy {
  holdFbDisbursements: IHoldFbDisbursement[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  searchText = '';
  selectedFbDisbursment!: IHoldFbDisbursement;

  constructor(
    protected holdFbDisbursementService: HoldFbDisbursementService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.holdFbDisbursements = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.holdFbDisbursementService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IHoldFbDisbursement[]>) => this.paginateHoldFbDisbursements(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.holdFbDisbursements = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHoldFbDisbursements();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHoldFbDisbursement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHoldFbDisbursements(): void {
    this.eventSubscriber = this.eventManager.subscribe('holdFbDisbursementListModification', () => this.reset());
  }

  delete(holdFbDisbursement: IHoldFbDisbursement): void {
    const modalRef = this.modalService.open(HoldFbDisbursementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.holdFbDisbursement = holdFbDisbursement;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateHoldFbDisbursements(data: IHoldFbDisbursement[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.holdFbDisbursements.push(data[i]);
      }
    }
  }

  search($event: any): void {
    this.searchText = $event;
    this.reset();
  }

  openVerticallyCentered(DisbursedFbDetails: any, holdFbDisbursement: IHoldFbDisbursement): void {
    this.modalService.open(DisbursedFbDetails, { centered: true, size: 'lg' });
    this.selectedFbDisbursment = holdFbDisbursement;
  }
}
