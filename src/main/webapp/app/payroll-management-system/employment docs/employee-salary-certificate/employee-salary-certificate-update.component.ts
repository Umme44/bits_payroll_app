import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

import { EmployeeSalaryCertificateService } from './employee-salary-certificate.service';
import { Status } from '../../../shared/model/enumerations/status.model';
import { swalOnRequestError, swalOnSavedSuccess, swalSuccessWithMessage } from '../../../shared/swal-common/swal-common';
import { ISalaryCertificate, SalaryCertificate } from '../../../shared/legacy/legacy-model/salary-certificate.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { IUser } from '../../../entities/user/user.model';
import { EmployeeSalaryService } from '../../../entities/employee-salary/service/employee-salary.service';
import { UserService } from '../../../entities/user/user.service';
import { EmployeeService } from '../../../entities/employee/service/employee.service';
import Swal from 'sweetalert2';
import {
  SWAL_DENIED_BTN_TIMER,
  SWAL_DENY_BUTTON_SELECT,
  SWAL_DENY_BUTTON_SELECT_ICON
} from '../../../shared/swal-common/swal.properties.constant';
import { IEmployeeSalary } from '../../../entities/employee-salary/employee-salary.model';

type SelectableEntity = IEmployeeSalary | IUser;

@Component({
  selector: 'jhi-employee-salary-certificate-update',
  templateUrl: './employee-salary-certificate-update.component.html',
})
export class EmployeeSalaryCertificateUpdateComponent implements OnInit {
  isSaving = false;
  employeesalaries: IEmployeeSalary[] = [];
  users: IUser[] = [];
  employees: IEmployee[] = [];
  createdAtDp: any;
  updatedAtDp: any;
  sanctionAtDp: any;

  editForm = this.fb.group({
    id: [],
    month: [],
    year: [],
    purpose: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(250)]],
    remarks: [null, [Validators.minLength(3), Validators.maxLength(250)]],
    status: [],
    employeeSalaryId: [null, Validators.required],
    employeeId: [null],
    createdAt: [],
    createdById: [],
    createdByLogin: [],
    updatedAt: [],
    updatedById: [],
    updatedByLogin: [],
    sanctionAt: [],
    sanctionById: [],
    sanctionByLogin: [],
  });

  constructor(
    protected employeeSalaryCertificateService: EmployeeSalaryCertificateService,
    protected employeeSalaryService: EmployeeSalaryService,
    protected userService: UserService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryCertificate }) => {
      this.employeeSalaryCertificateService.getListOfSalaries().subscribe(res => {
        this.employeesalaries = res.body!;
      });

      if (salaryCertificate.id) {
        this.employeeSalaryCertificateService.getEmployeeSalaryByCertificateId(salaryCertificate.id).subscribe(result => {
          this.employeesalaries.unshift(result.body!);

          this.updateForm(salaryCertificate);
        });
      }
    });
  }

  updateForm(salaryCertificate: ISalaryCertificate): void {
    this.editForm.patchValue({
      id: salaryCertificate.id,
      month: salaryCertificate.month,
      year: salaryCertificate.year,
      remarks: salaryCertificate.remarks as any,
      status: salaryCertificate.status,
      purpose: salaryCertificate.purpose as any,
      employeeSalaryId: salaryCertificate.salaryId as any,
      employeeId: salaryCertificate.employeeId,
      createdAt: salaryCertificate.createdAt,
      createdById: salaryCertificate.createdById,
      createdByLogin: salaryCertificate.createdByLogin,
      updatedAt: salaryCertificate.updatedAt,
      updatedById: salaryCertificate.updatedById,
      updatedByLogin: salaryCertificate.updatedByLogin,
      sanctionAt: salaryCertificate.sanctionAt,
      sanctionById: salaryCertificate.sanctionById,
      sanctionByLogin: salaryCertificate.sanctionByLogin,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salaryCertificate = this.createFromForm();
    if (salaryCertificate.id !== undefined && salaryCertificate.id !== null) {
      this.subscribeToSaveResponse(this.employeeSalaryCertificateService.update(salaryCertificate));
    } else {
      this.subscribeToSaveResponse(this.employeeSalaryCertificateService.create(salaryCertificate));
    }
  }

  private createFromForm(): ISalaryCertificate {
    return {
      ...new SalaryCertificate(),
      id: this.editForm.get(['id'])!.value,
      month: this.editForm.get(['month'])!.value,
      year: this.editForm.get(['year'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      status: this.editForm.get(['status'])!.value ?? Status.PENDING,
      purpose: this.editForm.get(['purpose'])!.value,
      salaryId: this.editForm.get(['employeeSalaryId'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
      createdByLogin: this.editForm.get(['createdByLogin'])!.value,
      updatedAt: this.editForm.get(['updatedAt'])!.value,
      updatedById: this.editForm.get(['updatedById'])!.value,
      updatedByLogin: this.editForm.get(['updatedByLogin'])!.value,
      sanctionAt: this.editForm.get(['sanctionAt'])!.value,
      sanctionById: this.editForm.get(['sanctionById'])!.value,
      sanctionByLogin: this.editForm.get(['sanctionByLogin'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalaryCertificate>>): void {
    result.subscribe({
      next: res => {
        this.onSaveSuccess();
      },
      error: err => {
        this.onSaveError(err);
      }
      }
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    swalSuccessWithMessage(
      'Your application for a salary certificate has been submitted. You will be notified via email once your application is approved.'
    );
    this.previousState();
  }

  protected onSaveError(err): void {
    this.isSaving = false;
    Swal.fire({
      icon: 'error',
      text: err.error,
      timer: 3500,
      showConfirmButton: false,
    })
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  onChangeEmployeeSalary(): void {
    const employeeSalaryID = this.editForm.get(['employeeSalaryId'])!.value;
    const employeesalary = this.employeesalaries.find(employeeSalary => employeeSalary.id === employeeSalaryID);
    this.editForm.get(['month'])!.setValue(employeesalary!.month);
    this.editForm.get(['year'])!.setValue(employeesalary!.year);
  }
}
