import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocation } from 'app/shared/model/location.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { LocationService } from './location.service';
import { LocationDeleteDialogComponent } from './location-delete-dialog.component';
import { EventManager } from '@angular/platform-browser';
import { ParseLinks } from '../../core/util/parse-links.service';

@Component({
  selector: 'jhi-location',
  templateUrl: './location.component.html',
})
export class LocationComponent implements OnInit, OnDestroy {
  locations: ILocation[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected locationService: LocationService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.locations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.locationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ILocation[]>) => this.paginateLocations(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.locations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLocations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      //this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILocation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLocations(): void {
    //this.eventSubscriber = this.eventManager.subscribe('locationListModification', () => this.reset());
  }

  delete(location: ILocation): void {
    const modalRef = this.modalService.open(LocationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.location = location;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLocations(data: ILocation[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.locations.push(data[i]);
      }
    }
  }
}
