import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import dayjs from 'dayjs/esm';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { countryList } from './listOfCountries';
import { CertificateStatus } from '../../../shared/model/enumerations/certificate-status.model';
import {
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSavedSuccess,
  swalOnUpdatedSuccess,
  swalSuccessWithMessage,
} from '../../../shared/swal-common/swal-common';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { IUser } from '../../../entities/user/user.model';
import { EmployeeNOCService } from './employee-noc.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { UserService } from '../../../entities/user/user.service';
import { EmployeeNOC, IEmployeeNOC } from '../../../shared/legacy/legacy-model/employee-noc.model';

type SelectableEntity = IEmployee | IUser;

@Component({
  selector: 'jhi-employee-noc-update',
  templateUrl: './employee-noc-update.component.html',
})
export class EmployeeNOCUpdateComponent implements OnInit {
  isSaving = false;
  isDisabled = false;
  employees: IEmployee[] = [];
  countries: string[] = [];
  leaveStartDateDp: any;
  leaveEndDateDp: any;
  issueDateDp: any;
  createdAtDp: any;
  updatedAtDp: any;
  generatedAtDp: any;

  stringOfSelectedCountries!: string;
  arrayOfSelectedCountries!: string[];

  leaveDuration!: number;
  hasLeaveDuration = false;
  isInvalidDateRange = false;
  hasApprovedLeaveApplication = true;

  editForm = this.fb.group({
    id: [],
    passportNumber: [null],
    leaveStartDate: [null],
    leaveEndDate: [null],
    leaveDuration: [],
    countryToVisit: [null],
    purposeOfNOC: [null],
    certificateStatus: [CertificateStatus.SENT_FOR_GENERATION],
    isRequiredForVisa: [false],
    referenceNumber: [null],
    issueDate: [],
    createdAt: [null],
    updatedAt: [],
    generatedAt: [],
    employeeId: [null],
    signatoryPersonId: [],
    createdById: [null],
    updatedById: [],
    generatedById: [],
  });

  constructor(
    protected employeeNOCService: EmployeeNOCService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.countries = countryList;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeNOC }) => {
      if (!employeeNOC.id) {
        const today = dayjs();
        this.isDisabled = true;
        employeeNOC.createdAt = today;
      } else {
        this.updateForm(employeeNOC);
        this.editForm.get('leaveDuration').disable();
        this.calculateLeaveDays();
      }
    });
  }

  updateForm(employeeNOC: IEmployeeNOC): void {
    this.setArrayOfCountries(employeeNOC);

    this.editForm.patchValue({
      id: employeeNOC.id,
      passportNumber: employeeNOC.passportNumber,
      leaveStartDate: employeeNOC.leaveStartDate,
      leaveEndDate: employeeNOC.leaveEndDate,
      countryToVisit: this.arrayOfSelectedCountries,
      purposeOfNOC: employeeNOC.purposeOfNOC,
      certificateStatus: employeeNOC.certificateStatus,
      isRequiredForVisa: employeeNOC.isRequiredForVisa,
      referenceNumber: employeeNOC.referenceNumber,
      issueDate: employeeNOC.issueDate,
      createdAt: employeeNOC.createdAt,
      updatedAt: employeeNOC.updatedAt,
      generatedAt: employeeNOC.generatedAt,
      employeeId: employeeNOC.employeeId,
      signatoryPersonId: employeeNOC.signatoryPersonId,
      createdById: employeeNOC.createdById,
      updatedById: employeeNOC.updatedById,
      generatedById: employeeNOC.generatedById,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeNOC = this.createFromForm();
    if (employeeNOC.id !== undefined && employeeNOC.id !== null) {
      this.subscribeToUpdateResponse(this.employeeNOCService.update(employeeNOC));
    } else {
      this.subscribeToSaveResponse(this.employeeNOCService.create(employeeNOC));
    }
  }

  onChangeDate(): void {
    this.isInvalidDateRange = false;
    this.hasApprovedLeaveApplication = true;
    const leaveStartDate = this.editForm.get(['leaveStartDate'])!.value;
    const leaveEndDate = this.editForm.get(['leaveEndDate'])!.value;

    if (leaveEndDate !== null && leaveStartDate !== undefined && leaveEndDate !== null && leaveEndDate !== undefined) {
      const duration = leaveEndDate.diff(leaveStartDate, 'days');
      if (duration < 0) {
        this.isInvalidDateRange = true;
        return;
      } else {
        this.calculateLeaveDays();
      }
      if (this.editForm.get(['purposeOfNOC'])!.value !== 'ACADEMIC' && this.editForm.get(['isRequiredForVisa'])!.value === false) {
        this.hasAnyApprovedLeaveApplicationBetweenDateRange(leaveStartDate, leaveEndDate);
      }
    }
  }

  calculateLeaveDays(): void {
    const leaveStartDate = this.editForm.get(['leaveStartDate'])!.value;
    const leaveEndDate = this.editForm.get(['leaveEndDate'])!.value;
    this.employeeNOCService.calculateLeaveDays(leaveStartDate, leaveEndDate).subscribe(res => {
      this.editForm.get(['leaveDuration'])!.setValue(res.body!);
    });
  }

  hasAnyApprovedLeaveApplicationBetweenDateRange(startDate: dayjs.Dayjs, endDate: dayjs.Dayjs): void {
    this.employeeNOCService.hasAnyApprovedLeaveApplicationWithinDateRange(startDate, endDate).subscribe(res => {
      if (res.body!) {
        this.hasApprovedLeaveApplication = true;
      } else {
        this.hasApprovedLeaveApplication = false;
      }
    });
  }

  private createFromForm(): IEmployeeNOC {
    this.getStringOfCountries();

    return {
      ...new EmployeeNOC(),
      id: this.editForm.get(['id'])!.value,
      passportNumber: this.editForm.get(['passportNumber'])!.value,
      leaveStartDate: this.editForm.get(['leaveStartDate'])!.value,
      leaveEndDate: this.editForm.get(['leaveEndDate'])!.value,
      countryToVisit: this.stringOfSelectedCountries,
      purposeOfNOC: this.editForm.get(['purposeOfNOC'])!.value,
      certificateStatus: this.editForm.get(['certificateStatus'])!.value,
      isRequiredForVisa: this.editForm.get(['isRequiredForVisa'])!.value,
      referenceNumber: this.editForm.get(['referenceNumber'])!.value,
      issueDate: this.editForm.get(['issueDate'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value,
      updatedAt: this.editForm.get(['updatedAt'])!.value,
      generatedAt: this.editForm.get(['generatedAt'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      signatoryPersonId: this.editForm.get(['signatoryPersonId'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
      updatedById: this.editForm.get(['updatedById'])!.value,
      generatedById: this.editForm.get(['generatedById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeNOC>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected subscribeToUpdateResponse(result: Observable<HttpResponse<IEmployeeNOC>>): void {
    result.subscribe(
      () => this.onUpdateSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    swalSuccessWithMessage(
      'Your application for employee NOC has been submitted. You will be notified via email once your application is approved.'
    );
    this.isSaving = false;
    this.previousState();
  }

  protected onUpdateSuccess(): void {
    swalOnUpdatedSuccess();
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(err: any): void {
    swalOnRequestErrorWithBackEndErrorTitle(err.error.title);
    this.isSaving = false;
  }

  getStringOfCountries(): void {
    const countryArray = this.editForm.get(['countryToVisit'])!.value;
    const stringOfCountries = countryArray.join(']&');
    this.stringOfSelectedCountries = stringOfCountries;
  }

  setArrayOfCountries(employeeNOC: IEmployeeNOC): void {
    const countryString = employeeNOC.countryToVisit!;
    const countryArray = countryString.split(']&');
    this.arrayOfSelectedCountries = countryArray;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  onChangePurpose(): void {
    this.onChangeDate();
  }

  onChangeIsRequiredForVisaApproval(): void {
    this.onChangeDate();
  }
}
