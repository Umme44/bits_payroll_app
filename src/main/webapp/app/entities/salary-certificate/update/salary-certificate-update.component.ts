import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SalaryCertificateFormGroup, SalaryCertificateFormService } from './salary-certificate-form.service';
import { ISalaryCertificate, NewSalaryCertificate } from '../salary-certificate.model';
import { SalaryCertificateService } from '../service/salary-certificate.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Status } from 'app/entities/enumerations/status.model';
import { Month } from 'app/entities/enumerations/month.model';
import {
  EmployeeSalaryCertificateService
} from '../../../payroll-management-system/employment docs/employee-salary-certificate/employee-salary-certificate.service';
import { IEmployeeSalary } from '../../employee-salary/employee-salary.model';

type SelectableEntity = IEmployeeSalary | IUser;

@Component({
  selector: 'jhi-salary-certificate-update',
  templateUrl: './salary-certificate-update.component.html',
})
export class SalaryCertificateUpdateComponent implements OnInit {
  isSaving = false;
  employeesalaries: IEmployeeSalary[] = [];
  isEmployeeSelected = false;
  salaryCertificate: ISalaryCertificate | null = null;
  statusValues = Object.keys(Status);
  monthValues = Object.keys(Month);

  usersSharedCollection: IUser[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm: SalaryCertificateFormGroup = this.salaryCertificateFormService.createSalaryCertificateFormGroup();

  @ViewChild('field_employeeSalary') field_employeeSalary: ElementRef;

  constructor(
    protected salaryCertificateService: SalaryCertificateService,
    protected salaryCertificateFormService: SalaryCertificateFormService,
    protected userService: UserService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected employeeSalaryCertificateService: EmployeeSalaryCertificateService
  ) {
    this.isEmployeeSelected = false;
  }

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryCertificate }) => {
      if (salaryCertificate) {
        this.salaryCertificateService.getEmployeeListOfSalaries(salaryCertificate.pin ).subscribe(res => {
          this.employeesalaries = res.body!;
        });
        this.employeeSalaryCertificateService.getEmployeeSalaryByCertificateId(salaryCertificate.id).subscribe(result => {
          this.employeesalaries.unshift(result.body!);
          this.updateForm(salaryCertificate);
          this.isEmployeeSelected = true;

          let salary_month = this.employeesalaries.find(item => (item.month == salaryCertificate.month && item.year == salaryCertificate.year));
          if(salary_month){
            this.field_employeeSalary.nativeElement.value = salary_month.id;
          }
        });

      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salaryCertificate = this.salaryCertificateFormService.getSalaryCertificate(this.editForm);
    if (salaryCertificate.id !== null) {
      console.log(salaryCertificate);
      this.subscribeToSaveResponse(this.salaryCertificateService.update(salaryCertificate));
    } else {
      salaryCertificate.status = Status.PENDING;
      this.subscribeToSaveResponse(this.salaryCertificateService.create(salaryCertificate as NewSalaryCertificate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalaryCertificate>>): void {
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

  protected updateForm(salaryCertificate: ISalaryCertificate): void {
    this.salaryCertificate = salaryCertificate;
    this.salaryCertificateFormService.resetForm(this.editForm, salaryCertificate);
    this.isEmployeeSelected = false
  }

  changeEmployee(employee: any): void {
    if (employee) {
      this.editForm.patchValue({employeeId: employee.id})
      this.getEmployeeListOfSalary(employee)
    }
  }

  getEmployeeListOfSalary(employee: any){
    this.salaryCertificateService.getEmployeeListOfSalaries(employee.pin).subscribe({
      next: res => {
        this.employeesalaries = res.body!;
        this.isEmployeeSelected = true;
      },
      error: err => {
        console.log(err);
      }
    })
  }

  onChangeEmployeeSalary(e: any): void {
    const employeesalary = this.employeesalaries.find(item => item.id.toString() === e.target.value);
    if(employeesalary){
      this.editForm.get(['month'])!.setValue(employeesalary!.month);
      this.editForm.get(['year'])!.setValue(employeesalary!.year);
    }
    else{
      this.editForm.get(['month'])!.setValue(null);
      this.editForm.get(['year'])!.setValue(null);
    }
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
