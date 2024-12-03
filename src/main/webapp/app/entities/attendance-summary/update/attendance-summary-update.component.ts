import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AttendanceSummaryFormGroup, AttendanceSummaryFormService } from './attendance-summary-form.service';
import { IAttendanceSummary } from '../attendance-summary.model';
import { AttendanceSummaryService } from '../service/attendance-summary.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-attendance-summary-update',
  templateUrl: './attendance-summary-update.component.html',
})
export class AttendanceSummaryUpdateComponent implements OnInit {
  isSaving = false;
  isUpdate: boolean;
  attendanceSummary: IAttendanceSummary | null = null;
  currentYear: number = new Date().getFullYear();
  employeesSharedCollection: IEmployee[] = [];
  monthLastDay = 31;
  months = [
    { Value: 1, Text: 'January' },
    { Value: 2, Text: 'February' },
    { Value: 3, Text: 'March' },
    { Value: 4, Text: 'April' },
    { Value: 5, Text: 'May' },
    { Value: 6, Text: 'June' },
    { Value: 7, Text: 'July' },
    { Value: 8, Text: 'August' },
    { Value: 9, Text: 'September' },
    { Value: 10, Text: 'October' },
    { Value: 11, Text: 'November' },
    { Value: 12, Text: 'December' },
  ];
  years: number[];
  editForm: AttendanceSummaryFormGroup = this.attendanceSummaryFormService.createAttendanceSummaryFormGroup();

  constructor(
    protected attendanceSummaryService: AttendanceSummaryService,
    protected attendanceSummaryFormService: AttendanceSummaryFormService,
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
    ];
  }

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendanceSummary }) => {
      this.attendanceSummary = attendanceSummary;
      if (attendanceSummary) {
        this.updateForm(attendanceSummary);
        // this.editForm.get('employeeId').disable();
        this.isUpdate = this.editForm.get('employeeId')!.value != null;
        this.editForm.get('month')?.disable();
        this.editForm.get('year')?.disable();
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendanceSummary = this.attendanceSummaryFormService.getAttendanceSummary(this.editForm);
    if (attendanceSummary.id !== null) {
      this.subscribeToSaveResponse(this.attendanceSummaryService.update(attendanceSummary));
    } else {
      this.subscribeToSaveResponse(this.attendanceSummaryService.create(attendanceSummary as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendanceSummary>>): void {
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

  updateForm(attendanceSummary: IAttendanceSummary): void {
    this.attendanceSummary = attendanceSummary;
    this.attendanceSummaryFormService.resetForm(this.editForm, attendanceSummary);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(this.employeesSharedCollection);
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees)))
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  monthDateValidation(): void {
    const month = this.editForm.get(['month'])!.value;
    const year = this.editForm.get(['month'])!.value;
    const totalFractionDays = this.editForm.get(['totalFractionDays'])!.value;
    if (month && year && totalFractionDays) {
      this.monthLastDay = new Date(year, month, 0).getDate();
      const totalFractionDaysControl = this.editForm.get(['totalFractionDays'])!;
      if (totalFractionDays > this.monthLastDay || totalFractionDays < 0) {
        totalFractionDaysControl.setValidators([Validators.required, Validators.min(0), Validators.max(this.monthLastDay)]);
      } else {
        totalFractionDaysControl.clearValidators();
      }
      totalFractionDaysControl.updateValueAndValidity();
    }
  }

  patchSelectedEmployeeId($event: any) {
    this.editForm.patchValue({ employeeId: $event.id });
  }
}
