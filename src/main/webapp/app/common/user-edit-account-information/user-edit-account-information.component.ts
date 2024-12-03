import { Component, OnInit, HostListener } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { EmployeeCommonService } from '../../common/employee-address-book/employee-common.service';
import { UserEditAccountInformationService } from './user-edit-account-information.service';
import { DomSanitizer } from '@angular/platform-browser';
import {
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
  SWAL_RESPONSE_ERROR_TIMER,
  SWAL_UPDATED_ICON,
  SWAL_UPDATED_TIMER,
} from 'app/shared/swal-common/swal.properties.constant';
import { swalOnUpdatedSuccess } from 'app/shared/swal-common/swal-common';
import Swal from 'sweetalert2';
import { IDesignation } from '../../shared/legacy/legacy-model/designation.model';
import { IDepartment } from '../../shared/legacy/legacy-model/department.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { IBand } from '../../shared/legacy/legacy-model/band.model';
import { INationality } from '../../entities/nationality/nationality.model';
import { IBankBranch } from '../../entities/bank-branch/bank-branch.model';
import { IUnit } from '../../entities/unit/unit.model';
import { IUserEditAccount, UserEditAccount } from './user-edit-account.model';
import {CustomValidator} from "../../validators/custom-validator";

type SelectableEntity = IDesignation | IDepartment | IEmployee | INationality | IBankBranch | IBand | IUnit;

@Component({
  selector: 'jhi-user-edit-account-information',
  templateUrl: './user-edit-account-information.component.html',
  styleUrls: ['user-edit-account-information.component.scss'],
})
export class UserEditAccountInformationComponent implements OnInit {
  employee: IEmployee | null = null;
  isSaving = false;
  bands: IBand[] = [];
  units: IUnit[] = [];
  dateOfBirthDp: any;

  imgSrc: any = '../../content/images/profile-placeholder.png';
  imgError = 'Showing error';

  syncingImage = false;

  editForm = this.fb.group({
    id: [],
    pin: [],
    fullName: [],
    surName: [],
    referenceId: [],
    picture: [],
    nationalIdNo: [],
    dateOfBirth: [],
    placeOfBirth: [],
    fatherName: [],
    motherName: [],
    bloodGroup: [],
    presentAddress: ["",[Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    permanentAddress: [ "",[Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    personalContactNo: ["",[Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]],
    personalEmail: ["",[Validators.pattern('^[a-zA-Z0-9.!#$%&amp;&#39;*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$')]],
    religion: [],
    maritalStatus: ["", [Validators.required]],
    dateOfMarriage: [],
    spouseName: ["", [Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    officialEmail: [],
    officialContactNo: [],
    officePhoneExtension: [],
    skypeId: ["", [Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    whatsappId: ["", [Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    emergencyContactPersonName: ["", [Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    emergencyContactPersonRelationshipWithEmployee: ["",[Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    emergencyContactPersonContactNumber: ["", [Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]],
    mainGrossSalary: [],
    employeeCategory: [],
    location: [],
    officeTimeDuration: [],
    checkInTime: [],
    checkOutTime: [],
    dateOfJoining: [],
    dateOfConfirmation: [],
    isProbationaryPeriodExtended: [],
    probationPeriodExtendedTo: [],
    payType: [],
    disbursementMethod: [],
    bankName: [],
    bankAccountNo: [],
    mobileCelling: [],
    bkashNumber: [],
    cardType: [],
    cardNumber: [],
    tinNumber: [],
    passportNo: [],
    passportPlaceOfIssue: [],
    passportIssuedDate: [],
    passportExpiryDate: [],
    gender: [],
    welfareFundDeduction: [],
    designationId: [],
    departmentId: [],
    reportingToId: [],
    nationalityId: [],
    bankBranchId: [],
    bandId: [],
    unitId: [],
  });

  constructor(
    protected userEditAccountInformationService: UserEditAccountInformationService,
    protected employeeService: EmployeeCommonService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected modalService: NgbModal,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.userEditAccountInformationService.find().subscribe((res: HttpResponse<IEmployee>) => {
        this.employee = res.body || null;
        if (this.employee) {
          this.updateForm(this.employee);
        }
      });
    });

    this.loadImage();
  }

  updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      referenceId: employee.referenceId,
      pin: employee.pin,
      picture: employee.picture,
      fullName: employee.fullName,
      surName: employee.surName,
      nationalIdNo: employee.nationalIdNo,
      dateOfBirth: employee.dateOfBirth,
      placeOfBirth: employee.placeOfBirth,
      fatherName: employee.fatherName,
      motherName: employee.motherName,
      bloodGroup: employee.bloodGroup,
      presentAddress: employee.presentAddress,
      permanentAddress: employee.permanentAddress,
      personalContactNo: employee.personalContactNo,
      personalEmail: employee.personalEmail,
      religion: employee.religion,
      maritalStatus: employee.maritalStatus,
      dateOfMarriage: employee.dateOfMarriage,
      spouseName: employee.spouseName,
      officialEmail: employee.officialEmail,
      officialContactNo: employee.officialContactNo,
      officePhoneExtension: employee.officePhoneExtension,
      whatsappId: employee.whatsappId,
      skypeId: employee.skypeId,
      emergencyContactPersonName: employee.emergencyContactPersonName,
      emergencyContactPersonRelationshipWithEmployee: employee.emergencyContactPersonRelationshipWithEmployee,
      emergencyContactPersonContactNumber: employee.emergencyContactPersonContactNumber,
      mainGrossSalary: employee.mainGrossSalary,
      employeeCategory: employee.employeeCategory,
      location: employee.location,
      dateOfJoining: employee.dateOfJoining,
      dateOfConfirmation: employee.dateOfConfirmation,
      isProbationaryPeriodExtended: employee.isProbationaryPeriodExtended,
      probationPeriodExtendedTo: employee.probationPeriodExtendedTo,
      payType: employee.payType,
      disbursementMethod: employee.disbursementMethod,
      bankName: employee.bankName,
      bankAccountNo: employee.bankAccountNo,
      mobileCelling: employee.mobileCelling,
      bkashNumber: employee.bkashNumber,
      cardType: employee.cardType,
      cardNumber: employee.cardNumber,
      tinNumber: employee.tinNumber,
      passportNo: employee.passportNo,
      passportPlaceOfIssue: employee.passportPlaceOfIssue,
      passportIssuedDate: employee.passportIssuedDate,
      passportExpiryDate: employee.passportExpiryDate,
      gender: employee.gender,
      welfareFundDeduction: employee.welfareFundDeduction,
      designationId: employee.designationId,
      departmentId: employee.departmentId,
      reportingToId: employee.reportingToId,
      nationalityId: employee.nationalityId,
      bankBranchId: employee.bankBranchId,
      bandId: employee.bandId,
      unitId: employee.unitId,
    });
  }

  loadImage(): void {
    this.employeeService.getCurrentDP().subscribe(
      data => {
        this.createImage(data);
      },
      error => {
        this.imgError = error;
      }
    );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.userEditAccountInformationService.update(employee));
    }
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  syncImage(): void {
    this.syncingImage = true;

    this.userEditAccountInformationService.syncImage().subscribe(
      res => {
        if (res.status === 200) {
          Swal.fire({
            icon: SWAL_UPDATED_ICON,
            text: 'Synced',
            timer: SWAL_UPDATED_TIMER,
            showConfirmButton: false,
          });
          this.loadImage();
        }
        this.syncingImage = false;
      },
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      err => {
        // Could not sync images;
        Swal.fire({
          icon: SWAL_RESPONSE_ERROR_ICON,
          text: SWAL_RESPONSE_ERROR_TEXT,
          timer: SWAL_RESPONSE_ERROR_TIMER,
          showConfirmButton: false,
        });
        this.syncingImage = false;
      }
    );
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  private createImage(image: Blob): void {
    if (image && image.size > 0) {
      const reader = new FileReader();

      reader.addEventListener(
        'load',
        () => {
          this.imgSrc = reader.result;
        },
        false
      );

      reader.readAsDataURL(image);
    }
  }

  private createFromForm(): IUserEditAccount {
    return {
      ...new UserEditAccount(),
      id: this.editForm.get(['id'])!.value,
      presentAddress: this.editForm.get(['presentAddress'])!.value,
      permanentAddress: this.editForm.get(['permanentAddress'])!.value,
      personalContactNo: this.editForm.get(['personalContactNo'])!.value,
      personalEmail: this.editForm.get(['personalEmail'])!.value,
      maritalStatus: this.editForm.get(['maritalStatus'])!.value,
      dateOfMarriage: this.editForm.get(['dateOfMarriage'])!.value,
      spouseName: this.editForm.get(['spouseName'])!.value,
      whatsappId: this.editForm.get(['whatsappId'])!.value,
      skypeId: this.editForm.get(['skypeId'])!.value,
      emergencyContactPersonName: this.editForm.get(['emergencyContactPersonName'])!.value,
      emergencyContactPersonRelationshipWithEmployee: this.editForm.get(['emergencyContactPersonRelationshipWithEmployee'])!.value,
      emergencyContactPersonContactNumber: this.editForm.get(['emergencyContactPersonContactNumber'])!.value,
    };
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    this.editForm.reset();
  }
}
