import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHolidays } from '../holidays.model';

@Component({
  selector: 'jhi-holidays-detail',
  templateUrl: './holidays-detail.component.html',
})
export class HolidaysDetailComponent implements OnInit {
  holidays: IHolidays | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ holidays }) => {
      this.holidays = holidays;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
