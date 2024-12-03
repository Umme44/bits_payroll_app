import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { EmploymentActionsService } from '../employment-actions.service';
import { IDesignation } from '../../../shared/legacy/legacy-model/designation.model';
import { IDepartment } from '../../../shared/legacy/legacy-model/department.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { IUnit } from '../../../shared/legacy/legacy-model/unit.model';
import { IBand } from '../../../shared/legacy/legacy-model/band.model';
import { DesignationService } from '../../../shared/legacy/legacy-service/designation.service';
import { DepartmentService } from '../../../shared/legacy/legacy-service/department.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { UnitService } from '../../../shared/legacy/legacy-service/unit.service';
import { BandService } from '../../../shared/legacy/legacy-service/band.service';
import { EmploymentHistory, IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';

type SelectableEntity = IDesignation | IDepartment | IEmployee | IUnit | IBand;

@Component({
  selector: 'jhi-employment-history-update',
  templateUrl: './transfer-update.component.html',
})
export class TransferUpdateComponent implements OnInit {
  isSaving = false;
  designations: IDesignation[] = [];
  departments: IDepartment[] = [];
  employees: IEmployee[] = [];
  units: IUnit[] = [];
  bands: IBand[] = [];
  effectiveDateDp: any;

  selectedEmployee: IEmployee | null = null;

  changedReportingToId!: number;
  // loadSelectedEmployee: Observable<any> | null = null ;

  // todo: form validation required for the following fields
  // effectiveDate: [null, [Validators.required]],
  // changedDepartmentId: [null, [Validators.required]],
  // employeeId: [null, Validators.required],
  // changedUnitId: [null, [Validators.required]],
  editForm = this.fb.group({
    id: [],
    referenceId: [],
    pin: [],
    eventType: [],
    effectiveDate: [null, [Validators.required]],
    previousMainGrossSalary: [],
    currentMainGrossSalary: [],
    previousWorkingHour: [],
    changedWorkingHour: [],
    previousDesignationId: [],
    changedDesignationId: [],
    previousDepartmentId: [],
    changedDepartmentId: [null, [Validators.required]],
    previousReportingToId: [],
    changedReportingToId: this.fb.group({
      employeeId: [],
    }),
    employeeId: [null, Validators.required],
    previousUnitId: [],
    changedUnitId: [null, [Validators.required]],
    previousBandId: [],
    changedBandId: [],
  });

  constructor(
    protected employmentActionsService: EmploymentActionsService,
    protected designationService: DesignationService,
    protected departmentService: DepartmentService,
    protected employeeService: EmployeeService,
    protected unitService: UnitService,
    protected bandService: BandService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  get changedReportingToForm(): FormGroup {
    return this.editForm.get('changedReportingToId') as FormGroup;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentHistory }) => {
      this.updateForm(employmentHistory);
      this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
      this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
      this.employeeService.find(employmentHistory.employeeId).subscribe((res: HttpResponse<IEmployee>) => {
        this.selectedEmployee = res.body || null;
      });
      this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));
      this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));
      this.changedReportingToId = employmentHistory.changedReportingToId;
      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(employmentHistory: IEmploymentHistory): void {
    this.editForm.patchValue({
      id: employmentHistory.id,
      referenceId: employmentHistory.referenceId,
      pin: employmentHistory.pin,
      eventType: employmentHistory.eventType,
      effectiveDate: employmentHistory.effectiveDate as any,
      previousMainGrossSalary: employmentHistory.previousMainGrossSalary,
      currentMainGrossSalary: employmentHistory.currentMainGrossSalary,
      previousWorkingHour: employmentHistory.previousWorkingHour,
      changedWorkingHour: employmentHistory.changedWorkingHour,
      previousDesignationId: employmentHistory.previousDesignationId,
      changedDesignationId: employmentHistory.changedDesignationId,
      previousDepartmentId: employmentHistory.previousDepartmentId as any,
      changedDepartmentId: employmentHistory.changedDepartmentId as any,
      previousReportingToId: employmentHistory.previousReportingToId,
      // changedReportingToId: employmentHistory.changedReportingToId, // todo: need to fix this
      employeeId: employmentHistory.employeeId as any,
      previousUnitId: employmentHistory.previousUnitId,
      changedUnitId: employmentHistory.changedUnitId as any,
      previousBandId: employmentHistory.previousBandId,
      changedBandId: employmentHistory.changedBandId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentHistory = this.createFromForm();
    if (employmentHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.employmentActionsService.updateTransfer(employmentHistory));
    } else {
      this.subscribeToSaveResponse(this.employmentActionsService.createTransfer(employmentHistory));
    }
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploymentHistory>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  private createFromForm(): IEmploymentHistory {
    return {
      ...new EmploymentHistory(),
      id: this.editForm.get(['id'])!.value,
      referenceId: this.editForm.get(['referenceId'])!.value,
      pin: this.editForm.get(['pin'])!.value,
      eventType: this.editForm.get(['eventType'])!.value,
      effectiveDate: this.editForm.get(['effectiveDate'])!.value,
      previousMainGrossSalary: this.editForm.get(['previousMainGrossSalary'])!.value,
      currentMainGrossSalary: this.editForm.get(['currentMainGrossSalary'])!.value,
      previousWorkingHour: this.editForm.get(['previousWorkingHour'])!.value,
      changedWorkingHour: this.editForm.get(['changedWorkingHour'])!.value,
      previousDesignationId: this.editForm.get(['previousDesignationId'])!.value,
      changedDesignationId: this.editForm.get(['changedDesignationId'])!.value,
      previousDepartmentId: this.editForm.get(['previousDepartmentId'])!.value,
      changedDepartmentId: this.editForm.get(['changedDepartmentId'])!.value,
      previousReportingToId: this.editForm.get(['previousReportingToId'])!.value,
      changedReportingToId: this.changedReportingToForm.get(['employeeId'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      previousUnitId: this.editForm.get(['previousUnitId'])!.value,
      changedUnitId: this.editForm.get(['changedUnitId'])!.value,
      previousBandId: this.editForm.get(['previousBandId'])!.value,
      changedBandId: this.editForm.get(['changedBandId'])!.value,
    };
  }
}
