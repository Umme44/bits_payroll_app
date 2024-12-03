import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeStaticFileFormGroup, EmployeeStaticFileFormService } from './employee-static-file-form.service';
import { IEmployeeStaticFile } from '../employee-static-file.model';
import { EmployeeStaticFileService } from '../service/employee-static-file.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-employee-static-file-update',
  templateUrl: './employee-static-file-update.component.html',
})
export class EmployeeStaticFileUpdateComponent implements OnInit {
  isSaving = false;
  employeeStaticFile: IEmployeeStaticFile | null = null;

  employeesSharedCollection: IEmployee[] = [];

  editForm: EmployeeStaticFileFormGroup = this.employeeStaticFileFormService.createEmployeeStaticFileFormGroup();

  constructor(
    protected employeeStaticFileService: EmployeeStaticFileService,
    protected employeeStaticFileFormService: EmployeeStaticFileFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeStaticFile }) => {
      this.employeeStaticFile = employeeStaticFile;
      if (employeeStaticFile) {
        this.updateForm(employeeStaticFile);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeStaticFile = this.employeeStaticFileFormService.getEmployeeStaticFile(this.editForm);
    if (employeeStaticFile.id !== null) {
      this.subscribeToSaveResponse(this.employeeStaticFileService.update(employeeStaticFile));
    } else {
      this.subscribeToSaveResponse(this.employeeStaticFileService.create(employeeStaticFile as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeStaticFile>>): void {
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

  protected updateForm(employeeStaticFile: IEmployeeStaticFile): void {
    this.employeeStaticFile = employeeStaticFile;
    this.employeeStaticFileFormService.resetForm(this.editForm, employeeStaticFile);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employeeStaticFile.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employeeStaticFile?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
