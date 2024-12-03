import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {ISpecialShiftTiming} from "../special-shift-timing.model";
import {SpecialShiftTimingService} from "../service/special-shift-timing.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {ParseLinks} from "../../../core/util/parse-links.service";
import {ITEMS_PER_PAGE} from "../../../config/pagination.constants";
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError
} from "../../../shared/swal-common/swal-common";
import {SpecialShiftTimingDetailModalComponent} from "../detail/special-shift-timing-detail-modal.component";



@Component({
  selector: 'jhi-special-shift-timing',
  templateUrl: './special-shift-timing.component.html',
})
export class SpecialShiftTimingComponent implements OnInit, OnDestroy {
  specialShiftTimings: ISpecialShiftTiming[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected specialShiftTimingService: SpecialShiftTimingService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.specialShiftTimings = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = false;
  }

  loadAll(): void {
    this.specialShiftTimingService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ISpecialShiftTiming[]>) => this.paginatespecialShiftTimings(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.specialShiftTimings = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSpecialShiftTimings();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISpecialShiftTiming): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSpecialShiftTimings(): void {
    this.eventSubscriber = this.eventManager.subscribe('specialShiftTimingListModification', () => this.reset());
  }

  delete(specialShiftTiming: ISpecialShiftTiming): void {
    // const modalRef = this.modalService.open(SpecialShiftTimingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    // modalRef.componentInstance.specialShiftTiming = specialShiftTiming;
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.specialShiftTimingService.delete(specialShiftTiming?.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.eventManager.broadcast('specialShiftTimingListModification');
          },
          () => swalOnRequestError()
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

  protected paginatespecialShiftTimings(data: ISpecialShiftTiming[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.specialShiftTimings.push(data[i]);
      }
    }
  }

  openView(specialShiftTiming: ISpecialShiftTiming): void {
    const modalRef = this.modalService.open(SpecialShiftTimingDetailModalComponent, { size: 'lg', backdrop: true });
    modalRef.componentInstance.specialShiftTiming = specialShiftTiming;
  }

  textSlicing(note: any): string {
    return note.slice(0, 30) + '...';
  }
}
