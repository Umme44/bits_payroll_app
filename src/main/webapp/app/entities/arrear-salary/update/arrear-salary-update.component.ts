import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ArrearSalaryFormGroup, ArrearSalaryFormService } from './arrear-salary-form.service';
import { IArrearSalary } from '../arrear-salary.model';
import { ArrearSalaryService } from '../service/arrear-salary.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Month } from 'app/entities/enumerations/month.model';

@Component({
  selector: 'jhi-arrear-salary-update',
  templateUrl: './arrear-salary-update.component.html',
})
export class ArrearSalaryUpdateComponent implements OnInit {
  isSaving = false;
  arrearSalary: IArrearSalary | null = null;
  monthValues = Object.keys(Month);

  employeesSharedCollection: IEmployee[] = [];

  years: number[];
  currentYear: number = new Date().getFullYear();

  editForm: ArrearSalaryFormGroup = this.arrearSalaryFormService.createArrearSalaryFormGroup();

  constructor(
    protected arrearSalaryService: ArrearSalaryService,
    protected arrearSalaryFormService: ArrearSalaryFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
      this.currentYear - 6,
      this.currentYear - 7,
      this.currentYear - 8,
      this.currentYear - 9,
      this.currentYear - 10,
    ];
  }

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearSalary }) => {
      this.arrearSalary = arrearSalary;
      if (arrearSalary) {
        this.updateForm(arrearSalary);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arrearSalary = this.arrearSalaryFormService.getArrearSalary(this.editForm);

    arrearSalary.employeeId = arrearSalary.employee.id;

    if (arrearSalary.id !== null) {
      this.subscribeToSaveResponse(this.arrearSalaryService.update(arrearSalary));
    } else {
      this.subscribeToSaveResponse(this.arrearSalaryService.create(arrearSalary as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArrearSalary>>): void {
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

  protected updateForm(arrearSalary: IArrearSalary): void {
    this.arrearSalary = arrearSalary;
    this.arrearSalaryFormService.resetForm(this.editForm, arrearSalary);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      arrearSalary.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.arrearSalary?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
