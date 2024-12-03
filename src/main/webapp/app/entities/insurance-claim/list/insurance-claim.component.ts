import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IInsuranceClaim} from "../insurance-claim.model";
import {EventManager} from "../../../core/util/event-manager.service";
import {ParseLinks} from "../../../core/util/parse-links.service";
import {InsuranceClaimService} from "../service/insurance-claim.service";
import {InsuranceClaimDeleteDialogComponent} from "../delete/insurance-claim-delete-dialog.component";
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-insurance-claim',
  templateUrl: './insurance-claim.component.html',
})
export class InsuranceClaimComponent implements OnInit, OnDestroy {
  insuranceClaims: IInsuranceClaim[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  searchText = '';
  selectedClaimStatus = 'ALL';
  selectedYear = 0;
  selectedMonth = 0;

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

  constructor(
    protected insuranceClaimService: InsuranceClaimService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.insuranceClaims = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;

    this.years = [
      this.currentYear + 1,
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];
  }

  loadAll(): void {
    this.insuranceClaimService
      .query(this.getClaimFilterRequestObject())
      .subscribe((res: HttpResponse<IInsuranceClaim[]>) => this.paginateInsuranceClaims(res.body, res.headers));
  }

  getClaimFilterRequestObject(): any {
    if (this.selectedClaimStatus === 'ALL') {
      return {
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
        page: this.page,
        size: this.itemsPerPage,
      };
    } else {
      return {
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
        status: this.selectedClaimStatus,
        page: this.page,
        size: this.itemsPerPage,
      };
    }
  }

  isSearchTextInvalid = false;
  reset(): void {
    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.searchText)){
      this.isSearchTextInvalid = true;
    }
    else{
      this.isSearchTextInvalid = false;
      this.page = 0;
      this.insuranceClaims = [];
      this.loadAll();
    }
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInsuranceClaims();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInsuranceClaim): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInsuranceClaims(): void {
    this.eventSubscriber = this.eventManager.subscribe('insuranceClaimListModification', () => this.reset());
  }

  delete(insuranceClaim: IInsuranceClaim): void {
    const modalRef = this.modalService.open(InsuranceClaimDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.insuranceClaim = insuranceClaim;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInsuranceClaims(data: IInsuranceClaim[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.insuranceClaims.push(data[i]);
      }
    }
  }

  onSearchTextChange(event: any): void {
    this.searchText = event.target.value;
    this.reset();
  }

  onChangeStatus(event: any): void {
    this.selectedClaimStatus = event.target.value;
    this.reset();
  }

  onChangeYear(event: any): void {
    this.selectedYear = Number.parseInt(event.target.value, 10);

    if (this.selectedYear === 0) {
      this.selectedMonth = 0;
    }

    this.reset();
  }

  onChangeMonth(event: any): void {
    this.selectedMonth = Number.parseInt(event.target.value, 10);
    this.reset();
  }
}
