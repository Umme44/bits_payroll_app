import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { EventManager } from 'app/core/util/event-manager.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';
import { HolidaysService } from '../../shared/legacy/legacy-service/holidays.service';
import { IHolidays } from '../../shared/legacy/legacy-model/holidays.model';

@Component({
  selector: 'jhi-holiday-calendar',
  templateUrl: './holiday-calendar.component.html',
  styleUrls: ['holiday-calendar.component.scss'],
})
export class HolidayCalendarComponent implements OnInit, OnDestroy {
  holidays?: IHolidays[];
  eventSubscriber?: Subscription;

  constructor(protected holidaysService: HolidaysService, protected eventManager: EventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.holidaysService.queryCommon().subscribe((res: HttpResponse<IHolidays[]>) => (this.holidays = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHolidays();
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

  holidayDurationCalculate(startDate: dayjs.Dayjs, endDate: number): number {
    return startDate.diff(endDate, 'days') + 1;
  }
}
