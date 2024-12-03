import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmploymentCertificateFormGroup, EmploymentCertificateFormService } from './employment-certificate-form.service';
import { IEmploymentCertificate } from '../employment-certificate.model';
import { EmploymentCertificateService } from '../service/employment-certificate.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { CertificateStatus } from 'app/entities/enumerations/certificate-status.model';

@Component({
  selector: 'jhi-employment-certificate-update',
  templateUrl: './employment-certificate-update.component.html',
})
export class EmploymentCertificateUpdateComponent implements OnInit {
  isSaving = false;
  employmentCertificate: IEmploymentCertificate | null = null;
  certificateStatusValues = Object.keys(CertificateStatus);

  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: EmploymentCertificateFormGroup = this.employmentCertificateFormService.createEmploymentCertificateFormGroup();

  constructor(
    protected employmentCertificateService: EmploymentCertificateService,
    protected employmentCertificateFormService: EmploymentCertificateFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentCertificate }) => {
      this.employmentCertificate = employmentCertificate;
      if (employmentCertificate) {
        this.updateForm(employmentCertificate);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentCertificate = this.employmentCertificateFormService.getEmploymentCertificate(this.editForm);
    if (employmentCertificate.id !== null) {
      this.subscribeToSaveResponse(this.employmentCertificateService.update(employmentCertificate));
    } else {
      this.subscribeToSaveResponse(this.employmentCertificateService.create(employmentCertificate as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploymentCertificate>>): void {
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

  protected updateForm(employmentCertificate: IEmploymentCertificate): void {
    this.employmentCertificate = employmentCertificate;
    this.employmentCertificateFormService.resetForm(this.editForm, employmentCertificate);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employmentCertificate.employee,
      employmentCertificate.signatoryPerson
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      employmentCertificate.createdBy,
      employmentCertificate.updatedBy,
      employmentCertificate.generatedBy
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
            this.employmentCertificate?.employee,
            this.employmentCertificate?.signatoryPerson
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
            this.employmentCertificate?.createdBy,
            this.employmentCertificate?.updatedBy,
            this.employmentCertificate?.generatedBy
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
