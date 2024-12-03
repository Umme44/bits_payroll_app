import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoomFormService, RoomFormGroup } from './room-form.service';
import { IRoom } from '../room.model';
import { RoomService } from '../service/room.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBuilding } from 'app/entities/building/building.model';
import { BuildingService } from 'app/entities/building/service/building.service';
import { IFloor } from 'app/entities/floor/floor.model';
import { FloorService } from 'app/entities/floor/service/floor.service';
import { IRoomType } from 'app/entities/room-type/room-type.model';
import { RoomTypeService } from 'app/entities/room-type/service/room-type.service';

@Component({
  selector: 'jhi-room-update',
  templateUrl: './room-update.component.html',
})
export class RoomUpdateComponent implements OnInit {
  isSaving = false;
  room: IRoom | null = null;

  usersSharedCollection: IUser[] = [];
  buildingsSharedCollection: IBuilding[] = [];
  floorsSharedCollection: IFloor[] = [];
  roomTypesSharedCollection: IRoomType[] = [];

  editForm: RoomFormGroup = this.roomFormService.createRoomFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected roomService: RoomService,
    protected roomFormService: RoomFormService,
    protected userService: UserService,
    protected buildingService: BuildingService,
    protected floorService: FloorService,
    protected roomTypeService: RoomTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareBuilding = (o1: IBuilding | null, o2: IBuilding | null): boolean => this.buildingService.compareBuilding(o1, o2);

  compareFloor = (o1: IFloor | null, o2: IFloor | null): boolean => this.floorService.compareFloor(o1, o2);

  compareRoomType = (o1: IRoomType | null, o2: IRoomType | null): boolean => this.roomTypeService.compareRoomType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ room }) => {
      this.room = room;
      if (room) {
        this.updateForm(room);
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
    const room = this.roomFormService.getRoom(this.editForm);
    if (room.id !== null) {
      this.subscribeToSaveResponse(this.roomService.update(room));
    } else {
      this.subscribeToSaveResponse(this.roomService.create(room as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>): void {
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

  protected updateForm(room: IRoom): void {
    this.room = room;
    this.roomFormService.resetForm(this.editForm, room);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      room.createdBy,
      room.updatedBy
    );
    this.buildingsSharedCollection = this.buildingService.addBuildingToCollectionIfMissing<IBuilding>(
      this.buildingsSharedCollection,
      room.building
    );
    this.floorsSharedCollection = this.floorService.addFloorToCollectionIfMissing<IFloor>(this.floorsSharedCollection, room.floor);
    this.roomTypesSharedCollection = this.roomTypeService.addRoomTypeToCollectionIfMissing<IRoomType>(
      this.roomTypesSharedCollection,
      room.roomType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.room?.createdBy, this.room?.updatedBy))
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.buildingService
      .query()
      .pipe(map((res: HttpResponse<IBuilding[]>) => res.body ?? []))
      .pipe(
        map((buildings: IBuilding[]) => this.buildingService.addBuildingToCollectionIfMissing<IBuilding>(buildings, this.room?.building))
      )
      .subscribe((buildings: IBuilding[]) => (this.buildingsSharedCollection = buildings));

    this.floorService
      .query()
      .pipe(map((res: HttpResponse<IFloor[]>) => res.body ?? []))
      .pipe(map((floors: IFloor[]) => this.floorService.addFloorToCollectionIfMissing<IFloor>(floors, this.room?.floor)))
      .subscribe((floors: IFloor[]) => (this.floorsSharedCollection = floors));

    this.roomTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoomType[]>) => res.body ?? []))
      .pipe(
        map((roomTypes: IRoomType[]) => this.roomTypeService.addRoomTypeToCollectionIfMissing<IRoomType>(roomTypes, this.room?.roomType))
      )
      .subscribe((roomTypes: IRoomType[]) => (this.roomTypesSharedCollection = roomTypes));
  }
}
