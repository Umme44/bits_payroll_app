import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeSalaryTempDataFormGroup, EmployeeSalaryTempDataFormService } from './employee-salary-temp-data-form.service';
import { IEmployeeSalaryTempData } from '../employee-salary-temp-data.model';
import { EmployeeSalaryTempDataService } from '../service/employee-salary-temp-data.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Month } from 'app/entities/enumerations/month.model';

@Component({
  selector: 'jhi-employee-salary-temp-data-update',
  templateUrl: './employee-salary-temp-data-update.component.html',
})
export class EmployeeSalaryTempDataUpdateComponent implements OnInit {
  isSaving = false;
  employeeSalaryTempData: IEmployeeSalaryTempData | null = null;
  monthValues = Object.keys(Month);

  employeesSharedCollection: IEmployee[] = [];

  editForm: EmployeeSalaryTempDataFormGroup = this.employeeSalaryTempDataFormService.createEmployeeSalaryTempDataFormGroup();

  constructor(
    protected employeeSalaryTempDataService: EmployeeSalaryTempDataService,
    protected employeeSalaryTempDataFormService: EmployeeSalaryTempDataFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeSalaryTempData }) => {
      this.employeeSalaryTempData = employeeSalaryTempData;
      if (employeeSalaryTempData) {
        this.updateForm(employeeSalaryTempData);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeSalaryTempData = this.employeeSalaryTempDataFormService.getEmployeeSalaryTempData(this.editForm);
    if (employeeSalaryTempData.id !== null) {
      this.subscribeToSaveResponse(this.employeeSalaryTempDataService.update(employeeSalaryTempData));
    } else {
      this.subscribeToSaveResponse(this.employeeSalaryTempDataService.create(employeeSalaryTempData as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeSalaryTempData>>): void {
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

  protected updateForm(employeeSalaryTempData: IEmployeeSalaryTempData): void {
    this.employeeSalaryTempData = employeeSalaryTempData;
    this.employeeSalaryTempDataFormService.resetForm(this.editForm, employeeSalaryTempData);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employeeSalaryTempData.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employeeSalaryTempData?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
