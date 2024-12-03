import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttendanceEntry } from '../attendance-entry.model';

@Component({
  selector: 'jhi-attendance-entry-detail',
  templateUrl: './attendance-entry-detail.component.html',
})
export class AttendanceEntryDetailComponent implements OnInit {
  attendanceEntry: IAttendanceEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendanceEntry }) => {
      this.attendanceEntry = attendanceEntry;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
