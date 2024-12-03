import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoomTypeFormService, RoomTypeFormGroup } from './room-type-form.service';
import { IRoomType } from '../room-type.model';
import { RoomTypeService } from '../service/room-type.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-room-type-update',
  templateUrl: './room-type-update.component.html',
})
export class RoomTypeUpdateComponent implements OnInit {
  isSaving = false;
  roomType: IRoomType | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: RoomTypeFormGroup = this.roomTypeFormService.createRoomTypeFormGroup();

  constructor(
    protected roomTypeService: RoomTypeService,
    protected roomTypeFormService: RoomTypeFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomType }) => {
      this.roomType = roomType;
      if (roomType) {
        this.updateForm(roomType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roomType = this.roomTypeFormService.getRoomType(this.editForm);
    if (roomType.id !== null) {
      this.subscribeToSaveResponse(this.roomTypeService.update(roomType));
    } else {
      this.subscribeToSaveResponse(this.roomTypeService.create(roomType as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomType>>): void {
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

  protected updateForm(roomType: IRoomType): void {
    this.roomType = roomType;
    this.roomTypeFormService.resetForm(this.editForm, roomType);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      roomType.createdBy,
      roomType.updatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.roomType?.createdBy, this.roomType?.updatedBy)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
