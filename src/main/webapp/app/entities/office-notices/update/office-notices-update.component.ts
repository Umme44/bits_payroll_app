import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OfficeNoticesFormService, OfficeNoticesFormGroup } from './office-notices-form.service';
import { IOfficeNotices } from '../office-notices.model';
import { OfficeNoticesService } from '../service/office-notices.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { NoticeStatus } from 'app/entities/enumerations/notice-status.model';

@Component({
  selector: 'jhi-office-notices-update',
  templateUrl: './office-notices-update.component.html',
})
export class OfficeNoticesUpdateComponent implements OnInit {
  isSaving = false;
  officeNotices: IOfficeNotices | null = null;
  noticeStatusValues = Object.keys(NoticeStatus);

  isDateInvalid = false;

  editForm: OfficeNoticesFormGroup = this.officeNoticesFormService.createOfficeNoticesFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected officeNoticesService: OfficeNoticesService,
    protected officeNoticesFormService: OfficeNoticesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ officeNotices }) => {
      this.officeNotices = officeNotices;
      if (officeNotices) {
        this.updateForm(officeNotices);
      }
    });
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
    const officeNotices = this.officeNoticesFormService.getOfficeNotices(this.editForm);
    if (officeNotices.id !== null) {
      this.subscribeToSaveResponse(this.officeNoticesService.update(officeNotices));
    } else {
      this.subscribeToSaveResponse(this.officeNoticesService.create(officeNotices as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOfficeNotices>>): void {
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

  protected updateForm(officeNotices: IOfficeNotices): void {
    this.officeNotices = officeNotices;
    this.officeNoticesFormService.resetForm(this.editForm, officeNotices);
  }
}
