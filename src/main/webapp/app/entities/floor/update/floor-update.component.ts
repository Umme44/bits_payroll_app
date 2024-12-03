import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FloorFormGroup, FloorFormService } from './floor-form.service';
import { IFloor } from '../floor.model';
import { FloorService } from '../service/floor.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBuilding } from 'app/entities/building/building.model';
import { BuildingService } from 'app/entities/building/service/building.service';

@Component({
  selector: 'jhi-floor-update',
  templateUrl: './floor-update.component.html',
})
export class FloorUpdateComponent implements OnInit {
  isSaving = false;
  floor: IFloor | null = null;

  usersSharedCollection: IUser[] = [];
  buildingsSharedCollection: IBuilding[] = [];

  editForm: FloorFormGroup = this.floorFormService.createFloorFormGroup();

  constructor(
    protected floorService: FloorService,
    protected floorFormService: FloorFormService,
    protected userService: UserService,
    protected buildingService: BuildingService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareBuilding = (o1: IBuilding | null, o2: IBuilding | null): boolean => this.buildingService.compareBuilding(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ floor }) => {
      this.floor = floor;
      if (floor) {
        this.updateForm(floor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const floor = this.floorFormService.getFloor(this.editForm);
    if (floor.id !== null) {
      this.subscribeToSaveResponse(this.floorService.update(floor));
    } else {
      this.subscribeToSaveResponse(this.floorService.create(floor as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFloor>>): void {
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

  protected updateForm(floor: IFloor): void {
    this.floor = floor;
    this.floorFormService.resetForm(this.editForm, floor);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      floor.createdBy,
      floor.updatedBy
    );
    this.buildingsSharedCollection = this.buildingService.addBuildingToCollectionIfMissing<IBuilding>(
      this.buildingsSharedCollection,
      floor.building
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.floor?.createdBy, this.floor?.updatedBy))
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.buildingService
      .query()
      .pipe(map((res: HttpResponse<IBuilding[]>) => res.body ?? []))
      .pipe(
        map((buildings: IBuilding[]) => this.buildingService.addBuildingToCollectionIfMissing<IBuilding>(buildings, this.floor?.building))
      )
      .subscribe((buildings: IBuilding[]) => (this.buildingsSharedCollection = buildings));
  }
}
