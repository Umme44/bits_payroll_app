import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {LeaveBalanceFormService, LeaveBalanceFormGroup} from './leave-balance-form.service';
import {ILeaveBalance} from '../leave-balance.model';
import {LeaveBalanceService} from '../service/leave-balance.service';
import {IEmployee} from 'app/entities/employee/employee.model';
import {EmployeeService} from 'app/entities/employee/service/employee.service';
import {LeaveType} from 'app/entities/enumerations/leave-type.model';
import {LeaveAmountType} from 'app/entities/enumerations/leave-amount-type.model';

import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'jhi-leave-balance-update',
  templateUrl: './leave-balance-update.component.html',
})
export class LeaveBalanceUpdateComponent implements OnInit {
  isSaving = false;
  leaveBalance: ILeaveBalance | null = null;
  leaveTypeValues = Object.keys(LeaveType);
  leaveAmountTypeValues = Object.keys(LeaveAmountType);

  employeesSharedCollection: IEmployee[] = [];
  selectedLeaveType = '';

  editForm: LeaveBalanceFormGroup = this.leaveBalanceFormService.createLeaveBalanceFormGroup();

  constructor(
    protected leaveBalanceService: LeaveBalanceService,
    protected leaveBalanceFormService: LeaveBalanceFormService,
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
    ]
  }

  currentYear: number = new Date().getFullYear();
  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);
  years: number[];

  ngOnInit(): void {
    this.editForm.controls['openingBalance'].disable();

    this.activatedRoute.data.subscribe(({leaveBalance}) => {
      this.leaveBalance = leaveBalance;
      if (leaveBalance) {
        this.updateForm(leaveBalance);


        this.editForm.controls['leaveType'].disable();
        this.editForm.controls['employeeId'].disable();
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveBalance = this.leaveBalanceFormService.getLeaveBalance(this.editForm);
    if (leaveBalance.id !== null) {
      this.subscribeToSaveResponse(this.leaveBalanceService.update(leaveBalance));
    } else {
      this.subscribeToSaveResponse(this.leaveBalanceService.create(leaveBalance as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveBalance>>): void {
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

  protected updateForm(leaveBalance: ILeaveBalance): void {

    if (this.editForm.get('id')!.value) {
      this.editForm.controls['leaveType'].disable();
      this.editForm.controls['employeeId'].disable();
    }

    this.leaveBalance = leaveBalance;
    this.leaveBalanceFormService.resetForm(this.editForm, leaveBalance);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      leaveBalance.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.leaveBalance?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  onChangeLeaveType(event: any): void {
    this.selectedLeaveType = event.target.value;
    if (this.selectedLeaveType === 'MENTIONABLE_ANNUAL_LEAVE') {
      this.editForm.controls['openingBalance'].enable();
      this.editForm.controls['openingBalance'].setValidators([Validators.required, Validators.min(0), Validators.max(366)]);
    } else {
      this.editForm.controls['openingBalance'].disable();
      this.editForm.controls['openingBalance'].clearValidators();
      this.editForm.patchValue({
        openingBalance: 0,
      });
    }
    this.editForm.controls['openingBalance'].updateValueAndValidity();
  }

  patchSelectedEmployee($event: any) {
    this.editForm.patchValue({
      employeeId: $event.id
    });
  }
}
