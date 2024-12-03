import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IOffer} from "../offer.model";
import {OfferService} from "../service/offer.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {ParseLinks} from "../../../core/util/parse-links.service";
import {OfferDeleteDialogComponent} from "../delete/offer-delete-dialog.component";

@Component({
  selector: 'jhi-offer',
  templateUrl: './offer.component.html',
})
export class OfferComponent implements OnInit, OnDestroy {
  offers: IOffer[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected offerService: OfferService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.offers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.offerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IOffer[]>) => this.paginateOffers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.offers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOffers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOffer): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOffers(): void {
    this.eventSubscriber = this.eventManager.subscribe('offerListModification', () => this.reset());
  }

  delete(offer: IOffer): void {
    const modalRef = this.modalService.open(OfferDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.offer = offer;
    modalRef.closed.subscribe(
      (res: HttpResponse<IOffer[]>) => {
        // this.onSuccess(res.body, res.headers, this.page, false),
        this.offers=[];
        this.loadPage(this.page);
      });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateOffers(data: IOffer[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.offers.push(data[i]);
      }
    }
  }

  getImageUrl(id: number): string {
    const resourceUrl = SERVER_API_URL + 'files/offer-image/' + id;
    return resourceUrl;
  }
}
