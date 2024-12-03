import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ItemInformationFormGroup, ItemInformationFormService } from './item-information-form.service';
import { IItemInformation } from '../item-information.model';
import { ItemInformationService } from '../service/item-information.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IUnitOfMeasurement } from 'app/entities/unit-of-measurement/unit-of-measurement.model';
import { UnitOfMeasurementService } from 'app/entities/unit-of-measurement/service/unit-of-measurement.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { swalOnSavedSuccess, swalOnUpdatedSuccess } from '../../../shared/swal-common/swal-common';

type SelectableEntity = IDepartment | IUnitOfMeasurement | IUser;

@Component({
  selector: 'jhi-item-information-update',
  templateUrl: './item-information-update.component.html',
})
export class ItemInformationUpdateComponent implements OnInit {
  isSaving = false;
  itemInformation: IItemInformation | null = null;

  departmentsSharedCollection: IDepartment[] = [];
  unitOfMeasurementsSharedCollection: IUnitOfMeasurement[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: ItemInformationFormGroup = this.itemInformationFormService.createItemInformationFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected itemInformationService: ItemInformationService,
    protected itemInformationFormService: ItemInformationFormService,
    protected departmentService: DepartmentService,
    protected unitOfMeasurementService: UnitOfMeasurementService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareUnitOfMeasurement = (o1: IUnitOfMeasurement | null, o2: IUnitOfMeasurement | null): boolean =>
    this.unitOfMeasurementService.compareUnitOfMeasurement(o1, o2);

  // compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  departments: IDepartment[] = [];
  unitOfMeasurements: IUnitOfMeasurement[] = [];
  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemInformation }) => {
      this.itemInformation = itemInformation;
      if (itemInformation) {
        this.updateForm(itemInformation);
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
    const itemInformation = this.itemInformationFormService.getItemInformation(this.editForm);
    if (itemInformation.id !== null) {
      this.subscribeToSaveResponse(this.itemInformationService.update(itemInformation));
    } else {
      this.subscribeToSaveResponse(this.itemInformationService.create(itemInformation as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemInformation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  /*  protected onSaveSuccess(): void {
    this.previousState();
  }*/
  protected onSaveSuccess(): void {
    this.isSaving = false;
    if (this.editForm.get('id')!.value) swalOnUpdatedSuccess();
    else swalOnSavedSuccess();
    this.isSaving = false;
    this.previousState();
  }

  /*  protected onSaveError(): void {
    // Api for inheritance.
  }*/
  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(itemInformation: IItemInformation): void {
    this.itemInformation = itemInformation;
    this.itemInformationFormService.resetForm(this.editForm, itemInformation);

    /*    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      itemInformation.department
    );
    this.unitOfMeasurementsSharedCollection = this.unitOfMeasurementService.addUnitOfMeasurementToCollectionIfMissing<IUnitOfMeasurement>(
      this.unitOfMeasurementsSharedCollection,
      itemInformation.unitOfMeasurement
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      itemInformation.createdBy,
      itemInformation.updatedBy
    );*/
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
    this.unitOfMeasurementService
      .query({
        size: Number.MAX_VALUE,
        page: 0,
        sort: ['id,asc'],
      })
      .subscribe((res: HttpResponse<IUnitOfMeasurement[]>) => (this.unitOfMeasurements = res.body || []));

    /* this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.itemInformation?.department)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.unitOfMeasurementService
      .query()
      .pipe(map((res: HttpResponse<IUnitOfMeasurement[]>) => res.body ?? []))
      .pipe(
        map((unitOfMeasurements: IUnitOfMeasurement[]) =>
          this.unitOfMeasurementService.addUnitOfMeasurementToCollectionIfMissing<IUnitOfMeasurement>(
            unitOfMeasurements,
            this.itemInformation?.unitOfMeasurement
          )
        )
      )
      .subscribe((unitOfMeasurements: IUnitOfMeasurement[]) => (this.unitOfMeasurementsSharedCollection = unitOfMeasurements));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.itemInformation?.createdBy, this.itemInformation?.updatedBy)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));*/
  }
}
