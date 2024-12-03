import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IManualAttendanceEntry } from '../../shared/legacy/legacy-model/manual-attendance-entry.model';

@Component({
  selector: 'jhi-manual-attendance-entry-detail',
  templateUrl: './manual-attendance-entry-detail.component.html',
})
export class ManualAttendanceEntryDetailComponent implements OnInit {
  manualAttendanceEntry: IManualAttendanceEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualAttendanceEntry }) => (this.manualAttendanceEntry = manualAttendanceEntry));
  }

  previousState(): void {
    window.history.back();
  }
}
