import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { EmploymentActionsService } from '../employment-actions.service';
import { swalSuccessWithMessage } from '../../../shared/swal-common/swal-common';
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
  selector: 'jhi-promotion',
  templateUrl: './promotion-create.component.html',
  styleUrls: ['promotion-create.component.scss'],
})
export class PromotionCreateComponent implements OnInit {
  isSaving = false;
  designations: IDesignation[] = [];
  departments: IDepartment[] = [];
  employees: IEmployee[] = [];
  units: IUnit[] = [];
  bands: IBand[] = [];
  effectiveDateDp: any;
  isvalid = true;
  bandvalues!: any;
  salary!: any;

  selectedEmployee: IEmployee | undefined;

  // todo : following validation required for these field
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
    previousBandId: [],
    changedBandId: [null, [Validators.required]],
  });

  constructor(
    protected employmentActionService: EmploymentActionsService,
    protected designationService: DesignationService,
    protected departmentService: DepartmentService,
    protected employeeService: EmployeeService,
    protected unitService: UnitService,
    protected bandService: BandService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));

    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));

    this.employeeService.getAllMinimalOfNgSelect().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

    this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));

    this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    //this.employeeIdForm.valueChanges.subscribe(id => this.loadEmplpoyee());
  }

  checkband(): void {
    const bandid = this.editForm.get(['changedBandId'])!.value;
    this.salary = this.editForm.get('currentMainGrossSalary')!.value;

    if (bandid != null && this.salary != null) {
      this.bandvalues = this.bands.find(x => x.id === bandid);
      if (this.salary < this.bandvalues.minSalary || this.salary > this.bandvalues.maxSalary) {
        this.isvalid = false;
      } else {
        this.isvalid = true;
      }
    }
    return;
  }

  updateForm(employmentHistory: IEmploymentHistory): void {
    this.editForm.patchValue({
      // id: employmentHistory.id,
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
      previousBandId: employmentHistory.previousBandId,
      changedBandId: employmentHistory.changedBandId as any,
    });
  }

  loadEmplpoyee(employeeId: number): void {
    if (employeeId !== undefined && employeeId !== null) {
      this.employeeService.find(employeeId).subscribe((res: HttpResponse<IEmployee>) => {
        this.selectedEmployee = res.body || undefined;
        if (this.selectedEmployee !== undefined) {
          this.editForm.patchValue({
            referenceId: this.selectedEmployee.referenceId,
            pin: this.selectedEmployee.pin,
            previousMainGrossSalary: this.selectedEmployee.mainGrossSalary,
            previousDesignationId: this.selectedEmployee.designationId,
            previousDepartmentId: this.selectedEmployee.departmentId,
            previousReportingToId: this.selectedEmployee.reportingToId,
            previousUnitId: this.selectedEmployee.unitId,
            previousBandId: this.selectedEmployee.bandId,
          });
        }
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentHistory = this.createFromForm();
    if (employmentHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.employmentActionService.updatePromotions(employmentHistory));
    } else {
      this.subscribeToSaveResponse(this.employmentActionService.createPromotions(employmentHistory));
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
    swalSuccessWithMessage('Promotion has initiated!');
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  private createFromForm(): IEmploymentHistory {
    return {
      ...new EmploymentHistory(),
      // id: this.editForm.get(['id'])!.value,
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

  changeEmployee(value: any): void {
    if (!value) {
      this.selectedEmployee = undefined;
    } else {
      this.selectedEmployee = value;
      this.loadEmplpoyee(this.selectedEmployee!.id!);
    }
  }
}
