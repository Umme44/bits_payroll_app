import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { EmploymentActionsService } from '../employment-actions.service';
import { swalOnUpdatedSuccess } from '../../../shared/swal-common/swal-common';
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
  templateUrl: './promotion-update.component.html',
})
export class PromotionUpdateComponent implements OnInit {
  isSaving = false;
  designations: IDesignation[] = [];
  departments: IDepartment[] = [];
  units: IUnit[] = [];
  bands: IBand[] = [];
  effectiveDateDp: any;
  selectedEmployee: IEmployee | null = null;

  isSalaryValid = true;
  bandvalues!: any;
  salary!: any;

  // todo: validatore required in following fields
  // effectiveDate: [null, [Validators.required]],
  // currentMainGrossSalary: [null, [Validators.required, Validators.max(9999999), Validators.min(0), Validators.pattern(/^\d*(?:[.,]\d{1,2})?$/)],],
  // changedDesignationId: [null, [Validators.required]],
  // changedBandId: [null, [Validators.required]],
  editForm = this.fb.group({
    id: [],
    referenceId: [],
    pin: [],
    eventType: [],
    effectiveDate: [null, [Validators.required]],
    previousMainGrossSalary: [],
    currentMainGrossSalary: [null, [Validators.required, Validators.max(9999999), Validators.min(0), Validators.pattern(/^\d*(?:[.,]\d{1,2})?$/)],],
    previousWorkingHour: [],
    changedWorkingHour: [],
    previousDesignationId: [],
    changedDesignationId: [null, [Validators.required]],
    previousDepartmentId: [],
    changedDepartmentId: [],
    previousReportingToId: [],
    changedReportingToId: [],
    employeeId: [],
    previousUnitId: [],
    changedUnitId: [],
    previousBandId: [null, [Validators.required]],
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

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentHistory }) => {
      this.updateForm(employmentHistory);
      this.employeeService.find(employmentHistory.employeeId).subscribe((res: HttpResponse<IEmployee>) => {
        this.selectedEmployee = res.body || null;
      });

      this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
      this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
      this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));
      this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));
    });
  }

  checkband(): void {
    const bandid = this.editForm.get(['changedBandId'])!.value;
    this.salary = this.editForm.get('currentMainGrossSalary')!.value;

    if (bandid != null && this.salary != null) {
      this.bandvalues = this.bands.find(x => x.id === bandid);
      if (this.salary < this.bandvalues.minSalary || this.salary > this.bandvalues.maxSalary) {
        this.isSalaryValid = false;
      } else {
        this.isSalaryValid = true;
      }
    }
    return;
  }

  updateForm(employmentHistory: IEmploymentHistory): void {
    this.editForm.patchValue({
      id: employmentHistory.id,
      referenceId: employmentHistory.referenceId,
      pin: employmentHistory.pin,
      eventType: employmentHistory.eventType,
      effectiveDate: employmentHistory.effectiveDate as any,
      previousMainGrossSalary: employmentHistory.previousMainGrossSalary,
      currentMainGrossSalary: employmentHistory.currentMainGrossSalary as any,
      previousWorkingHour: employmentHistory.previousWorkingHour,
      changedWorkingHour: employmentHistory.changedWorkingHour,
      previousDesignationId: employmentHistory.previousDesignationId,
      changedDesignationId: employmentHistory.changedDesignationId as any,
      previousDepartmentId: employmentHistory.previousDepartmentId,
      changedDepartmentId: employmentHistory.changedDepartmentId,
      previousReportingToId: employmentHistory.previousReportingToId,
      changedReportingToId: employmentHistory.changedReportingToId,
      employeeId: employmentHistory.employeeId,
      previousUnitId: employmentHistory.previousUnitId,
      changedUnitId: employmentHistory.changedUnitId,
      previousBandId: employmentHistory.previousBandId as any,
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
      this.subscribeToSaveResponse(this.employmentActionsService.updatePromotions(employmentHistory));
    } else {
      this.subscribeToSaveResponse(this.employmentActionsService.createPromotions(employmentHistory));
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
    swalOnUpdatedSuccess();
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
      changedReportingToId: this.editForm.get(['changedReportingToId'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      previousUnitId: this.editForm.get(['previousUnitId'])!.value,
      changedUnitId: this.editForm.get(['changedUnitId'])!.value,
      previousBandId: this.editForm.get(['previousBandId'])!.value,
      changedBandId: this.editForm.get(['changedBandId'])!.value,
    };
  }
}
