import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFlexSchedule } from '../flex-schedule.model';

@Component({
  selector: 'jhi-flex-schedule-detail',
  templateUrl: './flex-schedule-detail.component.html',
})
export class FlexScheduleDetailComponent implements OnInit {
  flexSchedule: IFlexSchedule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flexSchedule }) => {
      this.flexSchedule = flexSchedule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
