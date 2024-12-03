import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {ISpecialShiftTiming} from "../special-shift-timing.model";


@Component({
  selector: 'jhi-special-shift-timing-detail',
  templateUrl: './special-shift-timing-detail.component.html',
})
export class SpecialShiftTimingDetailComponent implements OnInit {
  specialShiftTiming: ISpecialShiftTiming | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specialShiftTimingSchedule }) => (this.specialShiftTiming = specialShiftTimingSchedule));
  }

  previousState(): void {
    window.history.back();
  }
}
