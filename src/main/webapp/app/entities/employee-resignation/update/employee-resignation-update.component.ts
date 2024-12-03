import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeResignationFormGroup, EmployeeResignationFormService } from './employee-resignation-form.service';
import { IEmployeeResignation } from '../employee-resignation.model';
import { EmployeeResignationService } from '../service/employee-resignation.service';
import { Status } from 'app/entities/enumerations/status.model';

import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import {Employee, IEmployee} from "../../../shared/legacy/legacy-model/employee.model";
import {EmployeeService} from "../../../shared/legacy/legacy-service/employee.service";

@Component({
  selector: 'jhi-employee-resignation-update',
  templateUrl: './employee-resignation-update.component.html',
})
export class EmployeeResignationUpdateComponent implements OnInit {
  isSaving = false;
  employeeResignation: IEmployeeResignation | null = null;
  statusValues = Object.keys(Status);

  employeesSharedCollection: IEmployee[] = [];

  editForm: EmployeeResignationFormGroup = this.employeeResignationFormService.createEmployeeResignationFormGroup();

  constructor(
    protected employeeResignationService: EmployeeResignationService,
    protected employeeResignationFormService: EmployeeResignationFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  /* compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2); */


  createdAtDp: any;
  updatedAtDp: any;
  resignationDateDp: any;
  lastWorkingDayDp: any;
  isNoticePeriodFollowed = true;
  isNoticePeriodCalculated = false;
  calculatedNoticePeriod = 0;
  categoryBasedNoticePeriod = 0;

  idDateValid = false;

  selectedEmployeeId!: number;
  selectedEmployee: IEmployee | null = null;


  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeResignation }) => {
      this.employeeResignation = employeeResignation;
      if (employeeResignation) {
        this.updateForm(employeeResignation);
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeResignation = this.employeeResignationFormService.getEmployeeResignation(this.editForm);
    if (employeeResignation.id !== null) {
      this.subscribeToSaveResponse(this.employeeResignationService.update(employeeResignation));
    } else {
      this.subscribeToSaveResponse(this.employeeResignationService.create(employeeResignation as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeResignation>>): void {
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

  protected updateForm(employeeResignation: IEmployeeResignation): void {
    this.employeeResignation = employeeResignation;
    this.employeeResignationFormService.resetForm(this.editForm, employeeResignation);

    //   this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   employeeResignation.createdBy,
    //   employeeResignation.uodatedBy,
    //   employeeResignation.employee
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.employeeService
    //   .getAllMinimal()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //         employees,
    //         this.employeeResignation?.createdBy,
    //         this.employeeResignation?.uodatedBy,
    //         this.employeeResignation?.employee
    //       )
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
    this.employeeService.getAllMinimal().subscribe(res => {
      this.employeesSharedCollection = res.body!;
    })
  }

  checkNoticePeriod(): void {
    this.isNoticePeriodCalculated = false;
    const employeeId = this.editForm.get(['employeeId'])!.value.employeeId;
    const resignationDate = this.editForm.get('resignationDate')!.value;
    const lastWorkingDay = this.editForm.get('lastWorkingDay')!.value;

    if (employeeId && resignationDate && lastWorkingDay) {
      this.employeeResignationService.getEmployeeNoticePeriod(employeeId).subscribe(response => {
        this.categoryBasedNoticePeriod = response;
        this.calculatedNoticePeriod = lastWorkingDay.diff(resignationDate, 'days');

        if (this.calculatedNoticePeriod < this.categoryBasedNoticePeriod) {
          this.isNoticePeriodFollowed = false;
        } else {
          this.isNoticePeriodFollowed = true;
        }
        if (this.calculatedNoticePeriod < 0) {
          this.calculatedNoticePeriod = 0;
        }
        this.isNoticePeriodCalculated = true;
      });
    }
  }

}
