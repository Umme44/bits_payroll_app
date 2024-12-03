import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';

import { UnitOfMeasurementFormService, UnitOfMeasurementFormGroup } from './unit-of-measurement-form.service';
import { IUnitOfMeasurement } from '../unit-of-measurement.model';
import { UnitOfMeasurementService } from '../service/unit-of-measurement.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { swalOnSavedSuccess, swalOnUpdatedSuccess } from '../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-unit-of-measurement-update',
  templateUrl: './unit-of-measurement-update.component.html',
})
export class UnitOfMeasurementUpdateComponent implements OnInit {
  isSaving = false;
  unitOfMeasurement: IUnitOfMeasurement | null = null;

  isNameUnique = true;
  uniqueNameCheckingTimeOut!: any;

  //usersSharedCollection: IUser[] = [];

  editForm: UnitOfMeasurementFormGroup = this.unitOfMeasurementFormService.createUnitOfMeasurementFormGroup();

  constructor(
    protected unitOfMeasurementService: UnitOfMeasurementService,
    protected unitOfMeasurementFormService: UnitOfMeasurementFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ unitOfMeasurement }) => {
      this.unitOfMeasurement = unitOfMeasurement;
      if (unitOfMeasurement) {
        this.updateForm(unitOfMeasurement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const unitOfMeasurement = this.unitOfMeasurementFormService.getUnitOfMeasurement(this.editForm);
    if (unitOfMeasurement.id !== null) {
      this.subscribeToSaveResponse(this.unitOfMeasurementService.update(unitOfMeasurement));
    } else {
      this.subscribeToSaveResponse(this.unitOfMeasurementService.create(unitOfMeasurement as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUnitOfMeasurement>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    if (this.editForm.get('id')!.value) swalOnUpdatedSuccess();
    else swalOnSavedSuccess();
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected updateForm(unitOfMeasurement: IUnitOfMeasurement): void {
    this.unitOfMeasurement = unitOfMeasurement;
    this.unitOfMeasurementFormService.resetForm(this.editForm, unitOfMeasurement);

    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   unitOfMeasurement.createdBy,
    //   unitOfMeasurement.updatedBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(users, this.unitOfMeasurement?.createdBy, this.unitOfMeasurement?.updatedBy)
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  checkNameIsUnique(): void {
    if (this.uniqueNameCheckingTimeOut) {
      clearTimeout(this.uniqueNameCheckingTimeOut);
    }
    if (this.editForm.get('name')!.valid) {
      this.uniqueNameCheckingTimeOut = setTimeout(() => {
        this.unitOfMeasurementService.checkNameIsUnique(this.editForm.get('name')!.value, this.editForm.get('id')!.value).subscribe(res => {
          this.isNameUnique = res.body!;
        });
      }, 1000);
    } else {
      this.isNameUnique = true;
    }
  }
}
