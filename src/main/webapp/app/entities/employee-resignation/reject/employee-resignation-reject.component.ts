import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import {IEmployee} from "../../employee/employee.model";
import {EmployeeResignationService} from "../service/employee-resignation.service";
import {EmployeeService} from "../../employee/service/employee.service";
import {IEmployeeResignation} from "../employee-resignation.model";
import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_SURE
} from "../../../shared/swal-common/swal.properties.constant";
import {DANGER_COLOR, PRIMARY_COLOR} from "../../../config/color.code.constant";
import {EmployeeResignation} from "../../../shared/legacy/legacy-model/employee-resignation.model";


@Component({
  selector: 'jhi-employee-resignation-reject',
  templateUrl: './employee-resignation-reject.component.html',
})
export class EmployeeResignationRejectComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];
  createdAtDp: any;
  updatedAtDp: any;
  resignationDateDp: any;
  lastWorkingDayDp: any;

  editForm = this.fb.group({
    id: [],
    createdAt: [],
    updatedAt: [],
    resignationDate: [],
    lastWorkingDay: [],
    approvalStatus: [],
    approvalComment: [null, [Validators.required]],
    isSalaryHold: [],
    isFestivalBonusHold: [],
    resignationReason: [],
    createdById: [],
    uodatedById: [],
    employeeId: [],
  });

  constructor(
    protected employeeResignationService: EmployeeResignationService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeResignation }) => {
      this.updateForm(employeeResignation);

      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(employeeResignation: IEmployeeResignation): void {
    this.editForm.patchValue({
      id: employeeResignation.id,
      createdAt: employeeResignation.createdAt,
      updatedAt: employeeResignation.updatedAt,
      resignationDate: employeeResignation.resignationDate,
      lastWorkingDay: employeeResignation.lastWorkingDay,
      approvalStatus: employeeResignation.approvalStatus,
      approvalComment: employeeResignation.approvalComment as any,
      isSalaryHold: employeeResignation.isSalaryHold,
      isFestivalBonusHold: employeeResignation.isFestivalBonusHold,
      resignationReason: employeeResignation.resignationReason,
      createdById: employeeResignation.createdById,
      uodatedById: employeeResignation.uodatedById,
      employeeId: employeeResignation.employeeId,
    });
  }

  resignationConfirmation(): void {
    Swal.fire({
      text: SWAL_SURE,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.save();
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeResignation = this.createFromForm();
    if (employeeResignation.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeResignationService.reject(employeeResignation));
    }
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeResignation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    Swal.fire({
      icon: 'success',
      html: 'Rejected! <br/> Populating data...',
      showConfirmButton: false,
      timer: 1500,
    });
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  private createFromForm(): IEmployeeResignation {
    return {
      ...new EmployeeResignation(),
      id: this.editForm.get(['id'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value,
      updatedAt: this.editForm.get(['updatedAt'])!.value,
      resignationDate: this.editForm.get(['resignationDate'])!.value,
      lastWorkingDay: this.editForm.get(['lastWorkingDay'])!.value,
      approvalStatus: this.editForm.get(['approvalStatus'])!.value,
      approvalComment: this.editForm.get(['approvalComment'])!.value,
      isSalaryHold: this.editForm.get(['isSalaryHold'])!.value,
      isFestivalBonusHold: this.editForm.get(['isFestivalBonusHold'])!.value,
      resignationReason: this.editForm.get(['resignationReason'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
      uodatedById: this.editForm.get(['uodatedById'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
    };
  }
}
