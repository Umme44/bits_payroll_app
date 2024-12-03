import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {IHolidays} from "../holidays.model";
import {EventManager} from "../../../core/util/event-manager.service";
import {HolidaysService} from "../service/holidays.service";
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError
} from "../../../shared/swal-common/swal-common";
@Component({
  selector: 'jhi-holidays',
  templateUrl: './holidays.component.html',
})
export class HolidaysComponent implements OnInit, OnDestroy {
  holidays?: IHolidays[];
  eventSubscriber?: Subscription;

  constructor(protected holidaysService: HolidaysService, protected eventManager: EventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.holidaysService.query().subscribe((res: HttpResponse<IHolidays[]>) => (this.holidays = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    //this.registerChangeInHolidays();
    this.loadAllOfAYear(new Date().getFullYear());
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHolidays): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHolidays(): void {
    this.eventSubscriber = this.eventManager.subscribe('holidaysListModification', () => this.loadAll());
  }

  delete(holidays: IHolidays): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.holidaysService.delete(holidays?.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadAll();
          },
          () => swalOnRequestError()
        );
      }
    });
  }

  loadAllOfAYear(value: any): void {
    this.holidaysService.queryByYear(Number(value)).subscribe((res: HttpResponse<IHolidays[]>) => (this.holidays = res.body || []));
  }
}
