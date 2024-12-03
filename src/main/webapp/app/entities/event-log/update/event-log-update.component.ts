import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EventLogFormGroup, EventLogFormService } from './event-log-form.service';
import { IEventLog } from '../event-log.model';
import { EventLogService } from '../service/event-log.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { RequestMethod } from 'app/entities/enumerations/request-method.model';

@Component({
  selector: 'jhi-event-log-update',
  templateUrl: './event-log-update.component.html',
})
export class EventLogUpdateComponent implements OnInit {
  isSaving = false;
  eventLog: IEventLog | null = null;
  requestMethodValues = Object.keys(RequestMethod);

  usersSharedCollection: IUser[] = [];

  editForm: EventLogFormGroup = this.eventLogFormService.createEventLogFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected eventLogService: EventLogService,
    protected eventLogFormService: EventLogFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventLog }) => {
      this.eventLog = eventLog;
      if (eventLog) {
        this.updateForm(eventLog);
      }

      this.loadRelationshipsOptions();
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
    const eventLog = this.eventLogFormService.getEventLog(this.editForm);
    if (eventLog.id !== null) {
      this.subscribeToSaveResponse(this.eventLogService.update(eventLog));
    } else {
      this.subscribeToSaveResponse(this.eventLogService.create(eventLog as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventLog>>): void {
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

  protected updateForm(eventLog: IEventLog): void {
    this.eventLog = eventLog;
    this.eventLogFormService.resetForm(this.editForm, eventLog);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, eventLog.performedBy);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.eventLog?.performedBy)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
