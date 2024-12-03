import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BuildingFormGroup, BuildingFormService } from './building-form.service';
import { IBuilding } from '../building.model';
import { BuildingService } from '../service/building.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-building-update',
  templateUrl: './building-update.component.html',
})
export class BuildingUpdateComponent implements OnInit {
  isSaving = false;
  building: IBuilding | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: BuildingFormGroup = this.buildingFormService.createBuildingFormGroup();

  constructor(
    protected buildingService: BuildingService,
    protected buildingFormService: BuildingFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ building }) => {
      this.building = building;
      if (building) {
        this.updateForm(building);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const building = this.buildingFormService.getBuilding(this.editForm);
    if (building.id !== null) {
      this.subscribeToSaveResponse(this.buildingService.update(building));
    } else {
      this.subscribeToSaveResponse(this.buildingService.create(building as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBuilding>>): void {
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

  protected updateForm(building: IBuilding): void {
    this.building = building;
    this.buildingFormService.resetForm(this.editForm, building);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      building.createdBy,
      building.updatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.building?.createdBy, this.building?.updatedBy)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
