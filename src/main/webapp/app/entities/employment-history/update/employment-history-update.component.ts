import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmploymentHistoryFormGroup, EmploymentHistoryFormService } from './employment-history-form.service';
import { IEmploymentHistory } from '../employment-history.model';
import { EmploymentHistoryService } from '../service/employment-history.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUnit } from 'app/entities/unit/unit.model';
import { UnitService } from 'app/entities/unit/service/unit.service';
import { IBand } from 'app/entities/band/band.model';
import { BandService } from 'app/entities/band/service/band.service';
import { EventType } from 'app/entities/enumerations/event-type.model';

@Component({
  selector: 'jhi-employment-history-update',
  templateUrl: './employment-history-update.component.html',
})
export class EmploymentHistoryUpdateComponent implements OnInit {
  isSaving = false;
  employmentHistory: IEmploymentHistory | null = null;
  eventTypeValues = Object.keys(EventType);

  designationsSharedCollection: IDesignation[] = [];
  departmentsSharedCollection: IDepartment[] = [];
  employeesSharedCollection: IEmployee[] = [];
  unitsSharedCollection: IUnit[] = [];
  bandsSharedCollection: IBand[] = [];

  editForm: EmploymentHistoryFormGroup = this.employmentHistoryFormService.createEmploymentHistoryFormGroup();

  constructor(
    protected employmentHistoryService: EmploymentHistoryService,
    protected employmentHistoryFormService: EmploymentHistoryFormService,
    protected designationService: DesignationService,
    protected departmentService: DepartmentService,
    protected employeeService: EmployeeService,
    protected unitService: UnitService,
    protected bandService: BandService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDesignation = (o1: IDesignation | null, o2: IDesignation | null): boolean => this.designationService.compareDesignation(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUnit = (o1: IUnit | null, o2: IUnit | null): boolean => this.unitService.compareUnit(o1, o2);

  compareBand = (o1: IBand | null, o2: IBand | null): boolean => this.bandService.compareBand(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentHistory }) => {
      this.employmentHistory = employmentHistory;
      if (employmentHistory) {
        this.updateForm(employmentHistory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentHistory = this.employmentHistoryFormService.getEmploymentHistory(this.editForm);
    if (employmentHistory.id !== null) {
      this.subscribeToSaveResponse(this.employmentHistoryService.update(employmentHistory));
    } else {
      this.subscribeToSaveResponse(this.employmentHistoryService.create(employmentHistory as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploymentHistory>>): void {
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

  protected updateForm(employmentHistory: IEmploymentHistory): void {
    this.employmentHistory = employmentHistory;
    this.employmentHistoryFormService.resetForm(this.editForm, employmentHistory);

    this.designationsSharedCollection = this.designationService.addDesignationToCollectionIfMissing<IDesignation>(
      this.designationsSharedCollection,
      employmentHistory.previousDesignation,
      employmentHistory.changedDesignation
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      employmentHistory.previousDepartment,
      employmentHistory.changedDepartment
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employmentHistory.previousReportingTo,
      employmentHistory.changedReportingTo,
      employmentHistory.employee
    );
    this.unitsSharedCollection = this.unitService.addUnitToCollectionIfMissing<IUnit>(
      this.unitsSharedCollection,
      employmentHistory.previousUnit,
      employmentHistory.changedUnit
    );
    this.bandsSharedCollection = this.bandService.addBandToCollectionIfMissing<IBand>(
      this.bandsSharedCollection,
      employmentHistory.previousBand,
      employmentHistory.changedBand
    );
  }

  protected loadRelationshipsOptions(): void {
    this.designationService
      .query()
      .pipe(map((res: HttpResponse<IDesignation[]>) => res.body ?? []))
      .pipe(
        map((designations: IDesignation[]) =>
          this.designationService.addDesignationToCollectionIfMissing<IDesignation>(
            designations,
            this.employmentHistory?.previousDesignation,
            this.employmentHistory?.changedDesignation
          )
        )
      )
      .subscribe((designations: IDesignation[]) => (this.designationsSharedCollection = designations));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
            departments,
            this.employmentHistory?.previousDepartment,
            this.employmentHistory?.changedDepartment
          )
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
            employees,
            this.employmentHistory?.previousReportingTo,
            this.employmentHistory?.changedReportingTo,
            this.employmentHistory?.employee
          )
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.unitService
      .query()
      .pipe(map((res: HttpResponse<IUnit[]>) => res.body ?? []))
      .pipe(
        map((units: IUnit[]) =>
          this.unitService.addUnitToCollectionIfMissing<IUnit>(
            units,
            this.employmentHistory?.previousUnit,
            this.employmentHistory?.changedUnit
          )
        )
      )
      .subscribe((units: IUnit[]) => (this.unitsSharedCollection = units));

    this.bandService
      .query()
      .pipe(map((res: HttpResponse<IBand[]>) => res.body ?? []))
      .pipe(
        map((bands: IBand[]) =>
          this.bandService.addBandToCollectionIfMissing<IBand>(
            bands,
            this.employmentHistory?.previousBand,
            this.employmentHistory?.changedBand
          )
        )
      )
      .subscribe((bands: IBand[]) => (this.bandsSharedCollection = bands));
  }
}
