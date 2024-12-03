import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IOfficeNotices} from "../../entities/office-notices/office-notices.model";
import {OfficeNoticesService} from "../../entities/office-notices/service/office-notices.service";
import {DataUtils} from "../../core/util/data-util.service";
import {EventManager} from "../../core/util/event-manager.service";
import {ParseLinks} from "../../core/util/parse-links.service";

@Component({
  selector: 'jhi-office-notices',
  templateUrl: './archive-office-notice-responsive.component.html',
  styleUrls: ['./archive-office-notices.component.scss', './archive-office-notice-responsive.component.scss'],
})
export class ArchiveOfficeNoticesComponent implements OnInit, OnDestroy {
  officeNotices: IOfficeNotices[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  links: any;

  constructor(
    protected officeNoticesService: OfficeNoticesService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
  }

  loadAll(): void {
    this.officeNoticesService
      .queryForUserEnd({
        page: this.page,
        size: this.itemsPerPage,
        //sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IOfficeNotices[]>) => this.paginateOfficeNotices(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.officeNotices = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  protected paginateOfficeNotices(data: IOfficeNotices[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.officeNotices.push(data[i]);
      }
    }
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOfficeNotices();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOfficeNotices): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInOfficeNotices(): void {
    this.eventSubscriber = this.eventManager.subscribe('officeNoticesListModification', () => this.reset());
  }

  /*sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }*/

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
