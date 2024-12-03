import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeNOCFormGroup, EmployeeNOCFormService } from './employee-noc-form.service';
import { IEmployeeNOC } from '../employee-noc.model';
import { EmployeeNOCService } from '../service/employee-noc.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { PurposeOfNOC } from 'app/entities/enumerations/purpose-of-noc.model';
import { CertificateStatus } from 'app/entities/enumerations/certificate-status.model';

@Component({
  selector: 'jhi-employee-noc-update',
  templateUrl: './employee-noc-update.component.html',
})
export class EmployeeNOCUpdateComponent implements OnInit {
  isSaving = false;
  employeeNOC: IEmployeeNOC | null = null;
  purposeOfNOCValues = Object.keys(PurposeOfNOC);
  certificateStatusValues = Object.keys(CertificateStatus);

  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: EmployeeNOCFormGroup = this.employeeNOCFormService.createEmployeeNOCFormGroup();

  constructor(
    protected employeeNOCService: EmployeeNOCService,
    protected employeeNOCFormService: EmployeeNOCFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeNOC }) => {
      this.employeeNOC = employeeNOC;
      if (employeeNOC) {
        this.updateForm(employeeNOC);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeNOC = this.employeeNOCFormService.getEmployeeNOC(this.editForm);
    if (employeeNOC.id !== null) {
      this.subscribeToSaveResponse(this.employeeNOCService.update(employeeNOC));
    } else {
      this.subscribeToSaveResponse(this.employeeNOCService.create(employeeNOC as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeNOC>>): void {
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

  protected updateForm(employeeNOC: IEmployeeNOC): void {
    this.employeeNOC = employeeNOC;
    this.employeeNOCFormService.resetForm(this.editForm, employeeNOC);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employeeNOC.employee,
      employeeNOC.signatoryPerson
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      employeeNOC.createdBy,
      employeeNOC.updatedBy,
      employeeNOC.generatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
            employees,
            this.employeeNOC?.employee,
            this.employeeNOC?.signatoryPerson
          )
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(
            users,
            this.employeeNOC?.createdBy,
            this.employeeNOC?.updatedBy,
            this.employeeNOC?.generatedBy
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
