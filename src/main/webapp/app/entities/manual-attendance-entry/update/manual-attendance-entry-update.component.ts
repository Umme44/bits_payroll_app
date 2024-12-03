import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ManualAttendanceEntryFormService, ManualAttendanceEntryFormGroup } from './manual-attendance-entry-form.service';
import { IManualAttendanceEntry } from '../manual-attendance-entry.model';
import { ManualAttendanceEntryService } from '../service/manual-attendance-entry.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-manual-attendance-entry-update',
  templateUrl: './manual-attendance-entry-update.component.html',
})
export class ManualAttendanceEntryUpdateComponent implements OnInit {
  isSaving = false;
  manualAttendanceEntry: IManualAttendanceEntry | null = null;

  employeesSharedCollection: IEmployee[] = [];

  editForm: ManualAttendanceEntryFormGroup = this.manualAttendanceEntryFormService.createManualAttendanceEntryFormGroup();

  constructor(
    protected manualAttendanceEntryService: ManualAttendanceEntryService,
    protected manualAttendanceEntryFormService: ManualAttendanceEntryFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualAttendanceEntry }) => {
      this.manualAttendanceEntry = manualAttendanceEntry;
      if (manualAttendanceEntry) {
        this.updateForm(manualAttendanceEntry);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manualAttendanceEntry = this.manualAttendanceEntryFormService.getManualAttendanceEntry(this.editForm);
    if (manualAttendanceEntry.id !== null) {
      this.subscribeToSaveResponse(this.manualAttendanceEntryService.update(manualAttendanceEntry));
    } else {
      this.subscribeToSaveResponse(this.manualAttendanceEntryService.create(manualAttendanceEntry as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManualAttendanceEntry>>): void {
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

  protected updateForm(manualAttendanceEntry: IManualAttendanceEntry): void {
    this.manualAttendanceEntry = manualAttendanceEntry;
    this.manualAttendanceEntryFormService.resetForm(this.editForm, manualAttendanceEntry);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      manualAttendanceEntry.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.manualAttendanceEntry?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
