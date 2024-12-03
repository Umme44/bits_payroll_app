import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoomRequisitionFormService, RoomRequisitionFormGroup } from './room-requisition-form.service';
import { IRoomRequisition } from '../room-requisition.model';
import { RoomRequisitionService } from '../service/room-requisition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-room-requisition-update',
  templateUrl: './room-requisition-update.component.html',
})
export class RoomRequisitionUpdateComponent implements OnInit {
  isSaving = false;
  roomRequisition: IRoomRequisition | null = null;
  statusValues = Object.keys(Status);

  usersSharedCollection: IUser[] = [];
  employeesSharedCollection: IEmployee[] = [];
  roomsSharedCollection: IRoom[] = [];

  editForm: RoomRequisitionFormGroup = this.roomRequisitionFormService.createRoomRequisitionFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected roomRequisitionService: RoomRequisitionService,
    protected roomRequisitionFormService: RoomRequisitionFormService,
    protected userService: UserService,
    protected employeeService: EmployeeService,
    protected roomService: RoomService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareRoom = (o1: IRoom | null, o2: IRoom | null): boolean => this.roomService.compareRoom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomRequisition }) => {
      this.roomRequisition = roomRequisition;
      if (roomRequisition) {
        this.updateForm(roomRequisition);
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
    const roomRequisition = this.roomRequisitionFormService.getRoomRequisition(this.editForm);
    if (roomRequisition.id !== null) {
      this.subscribeToSaveResponse(this.roomRequisitionService.update(roomRequisition));
    } else {
      this.subscribeToSaveResponse(this.roomRequisitionService.create(roomRequisition as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomRequisition>>): void {
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

  protected updateForm(roomRequisition: IRoomRequisition): void {
    this.roomRequisition = roomRequisition;
    this.roomRequisitionFormService.resetForm(this.editForm, roomRequisition);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      roomRequisition.createdBy,
      roomRequisition.updatedBy,
      roomRequisition.sanctionedBy
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      roomRequisition.requester
    );
    this.roomsSharedCollection = this.roomService.addRoomToCollectionIfMissing<IRoom>(this.roomsSharedCollection, roomRequisition.room);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(
            users,
            this.roomRequisition?.createdBy,
            this.roomRequisition?.updatedBy,
            this.roomRequisition?.sanctionedBy
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.roomRequisition?.requester)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.roomService
      .query()
      .pipe(map((res: HttpResponse<IRoom[]>) => res.body ?? []))
      .pipe(map((rooms: IRoom[]) => this.roomService.addRoomToCollectionIfMissing<IRoom>(rooms, this.roomRequisition?.room)))
      .subscribe((rooms: IRoom[]) => (this.roomsSharedCollection = rooms));
  }
}
