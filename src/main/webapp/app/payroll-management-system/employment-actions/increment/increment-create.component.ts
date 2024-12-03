import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { EmploymentActionsService } from '../employment-actions.service';
import { swalSuccessWithMessage } from '../../../shared/swal-common/swal-common';

import { EmploymentHistory, IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { IDepartment } from '../../../shared/legacy/legacy-model/department.model';
import { IDesignation } from '../../../shared/legacy/legacy-model/designation.model';
import { IUnit } from '../../../shared/legacy/legacy-model/unit.model';
import { IBand } from '../../../shared/legacy/legacy-model/band.model';
import { DesignationService } from '../../../shared/legacy/legacy-service/designation.service';
import { DepartmentService } from '../../../shared/legacy/legacy-service/department.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { UnitService } from '../../../shared/legacy/legacy-service/unit.service';
import { BandService } from '../../../shared/legacy/legacy-service/band.service';

type SelectableEntity = IDesignation | IDepartment | IEmployee | IUnit | IBand;

@Component({
  selector: 'jhi-increment',
  templateUrl: './increment-create.component.html',
})
export class IncrementCreateComponent implements OnInit {
  isSaving = false;
  bands: IBand[] = [];
  effectiveDateDp: any;

  selectedEmployee!: IEmployee;

  editForm = this.fb.group({
    id: [],
    referenceId: [],
    pin: [],
    eventType: [],
    effectiveDate: [null, [Validators.required]],
    previousMainGrossSalary: [],
    currentMainGrossSalary: [
      null,
      [Validators.required, Validators.max(9999999), Validators.min(0), Validators.pattern(/^\d*(?:[.,]\d{1,2})?$/)],
    ],
    previousWorkingHour: [],
    changedWorkingHour: [],
    previousDesignationId: [],
    changedDesignationId: [],
    previousDepartmentId: [],
    changedDepartmentId: [],
    previousReportingToId: [],
    changedReportingToId: [],
    employeeId: this.fb.group({
      employeeId: [],
    }),
    previousUnitId: [],
    changedUnitId: [],
    previousBandId: [],
    changedBandId: [null, [Validators.required]],
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

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  ngOnInit(): void {
    this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    this.employeeIdForm.valueChanges.subscribe(id => this.loadEmployee());
  }

  loadEmployee(): void {
    const employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    if (employeeId !== undefined && employeeId !== null) {
      this.employeeService.find(employeeId).subscribe((res: HttpResponse<IEmployee>) => {
        this.selectedEmployee = res.body!;
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
    this.subscribeToSaveResponse(this.employmentActionsService.createIncrement(employmentHistory));
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
    swalSuccessWithMessage('Increment has initiated');
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
      employeeId: this.employeeIdForm.get(['employeeId'])!.value,
      previousUnitId: this.editForm.get(['previousUnitId'])!.value,
      changedUnitId: this.editForm.get(['changedUnitId'])!.value,
      previousBandId: this.editForm.get(['previousBandId'])!.value,
      changedBandId: this.editForm.get(['changedBandId'])!.value,
    };
  }
}
