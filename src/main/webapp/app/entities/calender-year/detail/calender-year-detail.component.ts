import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICalenderYear } from '../calender-year.model';

@Component({
  selector: 'jhi-calender-year-detail',
  templateUrl: './calender-year-detail.component.html',
})
export class CalenderYearDetailComponent implements OnInit {
  calenderYear: ICalenderYear | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calenderYear }) => {
      this.calenderYear = calenderYear;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
