import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AttendanceSyncCacheFormGroup, AttendanceSyncCacheFormService } from './attendance-sync-cache-form.service';
import { IAttendanceSyncCache } from '../attendance-sync-cache.model';
import { AttendanceSyncCacheService } from '../service/attendance-sync-cache.service';

@Component({
  selector: 'jhi-attendance-sync-cache-update',
  templateUrl: './attendance-sync-cache-update.component.html',
})
export class AttendanceSyncCacheUpdateComponent implements OnInit {
  isSaving = false;
  attendanceSyncCache: IAttendanceSyncCache | null = null;

  editForm: AttendanceSyncCacheFormGroup = this.attendanceSyncCacheFormService.createAttendanceSyncCacheFormGroup();

  constructor(
    protected attendanceSyncCacheService: AttendanceSyncCacheService,
    protected attendanceSyncCacheFormService: AttendanceSyncCacheFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendanceSyncCache }) => {
      this.attendanceSyncCache = attendanceSyncCache;
      if (attendanceSyncCache) {
        this.updateForm(attendanceSyncCache);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendanceSyncCache = this.attendanceSyncCacheFormService.getAttendanceSyncCache(this.editForm);
    if (attendanceSyncCache.id !== null) {
      this.subscribeToSaveResponse(this.attendanceSyncCacheService.update(attendanceSyncCache));
    } else {
      this.subscribeToSaveResponse(this.attendanceSyncCacheService.create(attendanceSyncCache as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendanceSyncCache>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(attendanceSyncCache: IAttendanceSyncCache): void {
    this.attendanceSyncCache = attendanceSyncCache;
    this.attendanceSyncCacheFormService.resetForm(this.editForm, attendanceSyncCache);
  }
}
