import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TimeSlotFormGroup, TimeSlotFormService } from './time-slot-form.service';
import { ITimeSlot } from '../time-slot.model';
import { TimeSlotService } from '../service/time-slot.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { swalOnRequestErrorWithBackEndErrorTitle, swalOnSavedSuccess } from '../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-time-slot-update',
  templateUrl: './time-slot-update.component.html',
})
export class TimeSlotUpdateComponent implements OnInit {
  isSaving = false;
  timeSlot: ITimeSlot | null = null;

  isDuplicateTitle!: Boolean;
  saveExistsTitle?: string;

  weekEndList = [];

  weekDays: string[] = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
  isInvalid = false;

  editForm: TimeSlotFormGroup = this.timeSlotFormService.createTimeSlotFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected timeSlotService: TimeSlotService,
    protected timeSlotFormService: TimeSlotFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ timeSlot }) => {
      this.timeSlot = timeSlot;
      if (timeSlot) {
        this.updateForm(timeSlot);
      }
    });
  }

  checkTime(): void {
    const inTime = this.editForm.get(['inTime'])!.value;
    const outTime = this.editForm.get(['outTime'])!.value;

    if (inTime && outTime && inTime > outTime) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  checkDuplicateTitle($event: any): void {
    const title = this.editForm.get(['title'])!.value.trim();
    if (this.saveExistsTitle === title || title === '') {
      this.isDuplicateTitle = false;
    } else {
      this.timeSlotService.checkTitle(title).subscribe(res => {
        this.isDuplicateTitle = res.body!;
      });
    }
  }

  checkDuplicateBuildingNameOnInput($event: any): void {
    const title = this.editForm.get(['title'])!.value.trim();
    if (title === '') {
      this.isDuplicateTitle = false;
    }
  }

  checkParticipantList(event: any): void {
    this.weekEndList = this.editForm.get(['weekEndList'])!.value;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const timeSlot = this.timeSlotFormService.getTimeSlot(this.editForm);
    if (timeSlot.id !== null) {
      this.subscribeToSaveResponse(this.timeSlotService.update(timeSlot));
    } else {
      this.subscribeToSaveResponse(this.timeSlotService.create(timeSlot as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITimeSlot>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      res => this.onSaveError(res)
    );
  }

  protected onSaveSuccess(): void {
    swalOnSavedSuccess();
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(res: any): void {
    swalOnRequestErrorWithBackEndErrorTitle(res.error.title);
    this.isSaving = false;
  }

  protected updateForm(timeSlot: ITimeSlot): void {
    this.timeSlot = timeSlot;
    this.timeSlotFormService.resetForm(this.editForm, timeSlot);
  }
}
