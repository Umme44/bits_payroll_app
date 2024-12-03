import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VehicleRequisitionFormService, VehicleRequisitionFormGroup } from './vehicle-requisition-form.service';
import { IVehicleRequisition } from '../vehicle-requisition.model';
import { VehicleRequisitionService } from '../service/vehicle-requisition.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-vehicle-requisition-update',
  templateUrl: './vehicle-requisition-update.component.html',
})
export class VehicleRequisitionUpdateComponent implements OnInit {
  isSaving = false;
  vehicleRequisition: IVehicleRequisition | null = null;
  statusValues = Object.keys(Status);

  usersSharedCollection: IUser[] = [];
  employeesSharedCollection: IEmployee[] = [];
  vehiclesSharedCollection: IVehicle[] = [];

  editForm: VehicleRequisitionFormGroup = this.vehicleRequisitionFormService.createVehicleRequisitionFormGroup();

  constructor(
    protected vehicleRequisitionService: VehicleRequisitionService,
    protected vehicleRequisitionFormService: VehicleRequisitionFormService,
    protected userService: UserService,
    protected employeeService: EmployeeService,
    protected vehicleService: VehicleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleRequisition }) => {
      this.vehicleRequisition = vehicleRequisition;
      if (vehicleRequisition) {
        this.updateForm(vehicleRequisition);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleRequisition = this.vehicleRequisitionFormService.getVehicleRequisition(this.editForm);
    if (vehicleRequisition.id !== null) {
      this.subscribeToSaveResponse(this.vehicleRequisitionService.update(vehicleRequisition));
    } else {
      this.subscribeToSaveResponse(this.vehicleRequisitionService.create(vehicleRequisition as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleRequisition>>): void {
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

  protected updateForm(vehicleRequisition: IVehicleRequisition): void {
    this.vehicleRequisition = vehicleRequisition;
    this.vehicleRequisitionFormService.resetForm(this.editForm, vehicleRequisition);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      vehicleRequisition.createdBy,
      vehicleRequisition.updatedBy,
      vehicleRequisition.approvedBy
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      vehicleRequisition.requester
    );
    this.vehiclesSharedCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(
      this.vehiclesSharedCollection,
      vehicleRequisition.vehicle
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(
            users,
            this.vehicleRequisition?.createdBy,
            this.vehicleRequisition?.updatedBy,
            this.vehicleRequisition?.approvedBy
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.vehicleRequisition?.requester)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.vehicleService
      .query()
      .pipe(map((res: HttpResponse<IVehicle[]>) => res.body ?? []))
      .pipe(
        map((vehicles: IVehicle[]) =>
          this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.vehicleRequisition?.vehicle)
        )
      )
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesSharedCollection = vehicles));
  }
}
