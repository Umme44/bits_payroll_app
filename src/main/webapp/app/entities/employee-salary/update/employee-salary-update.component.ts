import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeSalaryFormGroup, EmployeeSalaryFormService } from './employee-salary-form.service';
import { IEmployeeSalary } from '../employee-salary.model';
import { EmployeeSalaryService } from '../service/employee-salary.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Month } from 'app/entities/enumerations/month.model';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';
import {ConfigService} from "../../config/service/config.service";
import {IAllowanceName} from "../../../shared/model/allowance-name.model";

@Component({
  selector: 'jhi-employee-salary-update',
  templateUrl: './employee-salary-update.component.html',
})
export class EmployeeSalaryUpdateComponent implements OnInit {
  isSaving = false;
  employeeSalary: IEmployeeSalary | null = null;
  monthValues = Object.keys(Month);
  employeeCategoryValues = Object.keys(EmployeeCategory);

  //employeesSharedCollection: IEmployee[] = [];

  employees: IEmployee[] = [];
  salaryGenerationDateDp: any;
  joiningDateDp: any;
  confirmationDateDp: any;
  NetBill = 0;

  Payablegroossalary = 0;
  grossbasic: any;
  grossmadical: any;
  grosshouse: any;
  grossconvanayanc: any;

  Totaldeduction = 0;
  PF: any;
  Tax: any;
  WelFare: any;
  MobileBill: any;
  Others: any;

  allowanceName: IAllowanceName | null = null;


  editForm: EmployeeSalaryFormGroup = this.employeeSalaryFormService.createEmployeeSalaryFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected employeeSalaryService: EmployeeSalaryService,
    protected employeeSalaryFormService: EmployeeSalaryFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected configService: ConfigService
  ) {}

  //compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.configService.getAllowanceName().subscribe(res => {
      this.allowanceName = res.body;
    });

    this.activatedRoute.data.subscribe(({ employeeSalary }) => {
      this.employeeSalary = employeeSalary;
      if (employeeSalary) {
        this.updateForm(employeeSalary);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeSalary = this.employeeSalaryFormService.getEmployeeSalary(this.editForm);
    if (employeeSalary.id !== null) {
      this.subscribeToSaveResponse(this.employeeSalaryService.update(employeeSalary));
    } else {
      this.subscribeToSaveResponse(this.employeeSalaryService.create(employeeSalary as any));
    }
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  loadPayableGrossSalary(): void {
    this.grossbasic = this.editForm.get(['payableGrossBasicSalary'])!.value;
    this.grossmadical = this.editForm.get(['payableGrossMedicalAllowance'])!.value;
    this.grosshouse = this.editForm.get(['payableGrossHouseRent'])!.value;
    this.grossconvanayanc = this.editForm.get(['payableGrossConveyanceAllowance'])!.value;
    this.Payablegroossalary = 0;

    if (this.grossbasic != null) this.Payablegroossalary += this.grossbasic;
    if (this.grosshouse != null) this.Payablegroossalary += this.grosshouse;
    if (this.grossconvanayanc != null) this.Payablegroossalary += this.grossconvanayanc;
    if (this.grossmadical != null) this.Payablegroossalary += this.grossmadical;

    this.editForm.controls['payableGrossSalary'].setValue(this.Payablegroossalary);

    if (this.editForm.get(['totalDeduction'])!.value != null) {
      this.editForm.controls['netPay'].setValue(this.Payablegroossalary - this.editForm.get(['totalDeduction'])!.value);
    }
    return;
  }

  loadTotalDeduction(): void {
    this.PF = this.editForm.get(['pfDeduction'])!.value;
    this.Tax = this.editForm.get(['taxDeduction'])!.value;
    this.WelFare = this.editForm.get(['welfareFundDeduction'])!.value;
    this.MobileBill = this.editForm.get(['mobileBillDeduction'])!.value;
    this.Others = this.editForm.get(['otherDeduction'])!.value;

    this.Totaldeduction = 0;
    if (this.PF != null) this.Totaldeduction += this.PF;
    if (this.Tax != null) this.Totaldeduction += this.Tax;
    if (this.WelFare != null) this.Totaldeduction += this.WelFare;
    if (this.MobileBill != null) this.Totaldeduction += this.MobileBill;
    if (this.Others != null) this.Totaldeduction += this.Others;

    this.editForm.controls['totalDeduction'].setValue(this.Totaldeduction);

    if (this.editForm.get(['payableGrossSalary'])!.value != null) {
      this.editForm.controls['netPay'].setValue(this.editForm.get(['payableGrossSalary'])!.value - this.Totaldeduction);
    }
    return;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeSalary>>): void {
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

  protected updateForm(employeeSalary: IEmployeeSalary): void {
    this.employeeSalary = employeeSalary;
    this.employeeSalaryFormService.resetForm(this.editForm, employeeSalary);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   employeeSalary.employee
    // );
  }

  protected loadRelationshipsOptions(): void {

    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employeeSalary?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
