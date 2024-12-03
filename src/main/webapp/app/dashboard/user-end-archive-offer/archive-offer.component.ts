import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IOffer} from "../../entities/offer/offer.model";
import {OfferService} from "../../entities/offer/service/offer.service";
import {EventManager} from "../../core/util/event-manager.service";
import {ParseLinks} from "../../core/util/parse-links.service";
@Component({
  selector: 'jhi-offer',
  templateUrl: './archive-offer.component.html',
  styleUrls: ['./archive-offer.component.scss'],
})
export class ArchiveOfferComponent implements OnInit, OnDestroy {
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
    this.predicate = 'createdAt';
    this.ascending = false;
  }

  loadAll(): void {
    this.offerService
      .archiveList({
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

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'createdAt') {
      result.push('createdAt');
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

  getOfferImage(offerId: number): string {
    const url = SERVER_API_URL + 'files/offer-image/' + offerId;
    return url;
  }
}
