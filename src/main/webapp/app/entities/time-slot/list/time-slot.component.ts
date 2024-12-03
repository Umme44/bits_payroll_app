import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITimeSlot } from '../time-slot.model';
import { TimeSlotService } from '../service/time-slot.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-time-slot',
  templateUrl: './time-slot.component.html',
})
export class TimeSlotComponent implements OnInit, OnDestroy {
  timeSlots: ITimeSlot[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected timeSlotService: TimeSlotService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.timeSlots = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.timeSlotService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ITimeSlot[]>) => this.paginateTimeSlots(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.timeSlots = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTimeSlots();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITimeSlot): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTimeSlots(): void {
    this.eventSubscriber = this.eventManager.subscribe('timeSlotListModification', () => this.reset());
  }

  delete(timeSlot: ITimeSlot): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.timeSlotService.delete(timeSlot.id!).subscribe(
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

  protected paginateTimeSlots(data: ITimeSlot[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.timeSlots.push(data[i]);
      }
    }
  }
}
