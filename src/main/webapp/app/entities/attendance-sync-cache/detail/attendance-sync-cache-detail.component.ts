import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttendanceSyncCache } from '../attendance-sync-cache.model';

@Component({
  selector: 'jhi-attendance-sync-cache-detail',
  templateUrl: './attendance-sync-cache-detail.component.html',
})
export class AttendanceSyncCacheDetailComponent implements OnInit {
  attendanceSyncCache: IAttendanceSyncCache | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendanceSyncCache }) => {
      this.attendanceSyncCache = attendanceSyncCache;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
