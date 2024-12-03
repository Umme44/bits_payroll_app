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
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { BandService } from '../../../shared/legacy/legacy-service/band.service';
import { EmploymentHistory, IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';

type SelectableEntity = IDesignation | IDepartment | IEmployee | IUnit | IBand;

@Component({
  selector: 'jhi-employment-history-update',
  templateUrl: './increment-update.component.html',
})
export class IncrementUpdateComponent implements OnInit {
  isSaving = false;
  bands: IBand[] = [];
  effectiveDateDp: any;
  selectedEmployee!: IEmployee;

  // todo: need validation in the form
  editForm = this.fb.group({
    id: [],
    referenceId: [],
    pin: [],
    eventType: [],
    effectiveDate: [null, [Validators.required]],
    previousMainGrossSalary: [null],
    currentMainGrossSalary: [
      '',
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
    employeeId: [],
    previousUnitId: [],
    changedUnitId: [],
    previousBandId: [],
    changedBandId: [null, Validators.required],
  });

  constructor(
    protected employmentActionsService: EmploymentActionsService,
    protected employeeService: EmployeeService,
    protected bandService: BandService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentHistory }) => {
      this.updateForm(employmentHistory);

      if (employmentHistory.id && employmentHistory.employeeId) {
        this.employeeService.find(employmentHistory.employeeId).subscribe((res: HttpResponse<IEmployee>) => {
          this.selectedEmployee = res.body!;
        });
      }
      this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));
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
      currentMainGrossSalary: employmentHistory.currentMainGrossSalary as any,
      previousWorkingHour: employmentHistory.previousWorkingHour,
      changedWorkingHour: employmentHistory.changedWorkingHour,
      previousDesignationId: employmentHistory.previousDesignationId,
      changedDesignationId: employmentHistory.changedDesignationId,
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentHistory = this.createFromForm();
    if (employmentHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.employmentActionsService.updateIncrement(employmentHistory));
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
