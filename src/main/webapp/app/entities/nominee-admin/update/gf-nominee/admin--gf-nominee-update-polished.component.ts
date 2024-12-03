import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

import { NomineeFormService } from '../edit-form-service/nominee-form.service';
import { INominee, Nominee } from '../../nominee.model';
import { IEmployee } from '../../../employee-custom/employee-custom.model';
import { NomineeType } from '../../../../shared/model/enumerations/nominee-type.model';
import { IdentityType } from '../../../../shared/model/enumerations/identity-type.model';
import { IdentityNumberValidationService, IIdentityNumberValidation } from '../../../../shared/service/identity-number-validation.service';
import { NomineeService } from '../../service/nominee.service';
import { NomineeValidationService } from '../../service/nominee-validation.service';
import { EmployeeCustomService } from '../../../employee-custom/service/employee-custom.service';
import { DateValidationService } from '../../../../shared/service/date-validation.service';
import { ACCESS_GROUP_EMPLOYEE } from '../../routes/nominee.route';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading,
  swalOnRejectionForNomineeReport,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSavedSuccess,
} from '../../../../shared/swal-common/swal-common';
import { INomineeValidation } from '../../../../shared/model/nominee-validation.model';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-admin-gf-nominee-update-polished',
  templateUrl: './admin-gf-nominee-update-polished.component.html',
})
export class AdminGfNomineeUpdatePolishedComponent implements OnInit, OnDestroy {
  isSaving = false;
  routerNominee!: INominee;
  employees: IEmployee[] = [];
  dateOfBirthDp: any;
  guardianDateOfBirthDp: any;
  nominationDateDp: any;
  nomineeType!: NomineeType;
  identityType!: IdentityType;
  selectedIdentityType: string | null = null;
  selectedGuardianIdentityType: string | null = null;
  accessGroup = '';
  totalConsumedPercentage = 0;
  selectedEmployeeId!: number;
  inValidFile = false;
  inValidFileErrorMsg = '';
  isNomineeImageMissing = true;
  nomineeAge = 0;
  showGuardianInputFields = false;
  nomineeImage!: File;
  headerName!: string;
  routeName!: string;
  employeePIN!: string;

  editForm = this.nomineeFormService.createNomineeFormGroup();
  nominees: INominee[] = [];

  urlForNomineeImg!: string;
  changeNomineeImage = true;
  invalidGuardianAge = false;

  nomineeIDNumberValidation: IIdentityNumberValidation;
  guardianIDNumberValidation: IIdentityNumberValidation;
  dobMaxDate: any;

  constructor(
    public nomineeService: NomineeService,
    public nomineeValidationService: NomineeValidationService,
    public identityNumberValidationService: IdentityNumberValidationService,
    protected employeeService: EmployeeCustomService,
    protected dateValidationService: DateValidationService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected nomineeFormService: NomineeFormService,
    private fb: FormBuilder
  ) {
    const nomineeTypeFromRoute = this.activatedRoute.snapshot.data['nomineeType'];
    nomineeTypeFromRoute ? (this.nomineeType = nomineeTypeFromRoute) : this.router.navigate(['/dashboard']);
    this.editForm.get('nomineeType')!.setValue(this.nomineeType);

    const accessGroupFromRoute = this.activatedRoute.snapshot.data['accessGroup'];
    accessGroupFromRoute ? (this.accessGroup = accessGroupFromRoute) : this.router.navigate(['/dashboard']);

    this.nomineeIDNumberValidation = { isValid: true, validationMsg: '' };
    this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };

    this.dobMaxDate = dateValidationService.getDOBMaxDate();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nominee }) => {
      this.updateForm(nominee);

      if (!nominee.id) {
        this.editForm.get('identityType')!.setValue(null);
        this.editForm.get('guardianIdentityType')!.setValue(null);
      }

      if (nominee.id) {
        this.loadSavedImage(nominee.id);
        this.selectedEmployeeId = nominee.employeeId!;
        this.onChangeDateOfBirth(nominee);
      }

      if (nominee.identityType === IdentityType.OTHER) {
        this.selectedIdentityType = 'OTHER';
        this.updateValidationOnNomineeIdentity();
      }

      if (nominee.guardianIdentityType === IdentityType.OTHER) {
        this.selectedGuardianIdentityType = 'OTHER';
        this.updateValidationOnGuardianIdentityType();
      }

      if (nominee.age) {
        this.nomineeAge = nominee.age;
        this.updateValidationForGuardian();
      }
    });
    if (this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
      this.loadNomineeList();
    }

    this.loadUsedSharePercentage();

    this.editForm.get('age')!.disable();

    this.employeeService.getEmployees().subscribe((res: HttpResponse<IEmployee[]>) => {
      this.employees = res.body || [];
      this.employees = this.employees.map(item => {
        return {
          id: item.id,
          pin: item.pin,
          name: item.fullName,
          designation: item.designationName,
          fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
        };
      });
    });

    if (this.editForm.get(['id'])!.value) {
      this.editForm.get(['employeeId'])!.disable();
      //this.editForm.get(['sharePercentage'])!.disable();
    }

    if (sessionStorage.getItem('midHeaderOfNomineeReport') !== null && sessionStorage.getItem('midHeaderOfNomineeReport') !== undefined) {
      this.headerName = sessionStorage.getItem('midHeaderOfNomineeReport')!;
    }

    if (sessionStorage.getItem('midRouteOfNomineeReport') !== null && sessionStorage.getItem('midRouteOfNomineeReport') !== undefined) {
      this.routeName = sessionStorage.getItem('midRouteOfNomineeReport')!;
    }
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('midHeaderOfNomineeReport');
    sessionStorage.removeItem('midRouteOfNomineeReport');
  }

  ///      List of Nominee    ///

  loadNomineeList(): void {
    const nominee: INominee = {
      ...new Nominee(),
      employeeId: this.selectedEmployeeId,
      nomineeType: this.nomineeType,
    };
    if (this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
      this.nomineeService.getNomineeListAdmin(nominee).subscribe(res => {
        this.nominees = res.body || [];
        if (this.nominees.length > 0) {
          this.employeePIN = this.nominees[0]!.pin!;
        }
      });
    }
  }

  loadUsedSharePercentage(): void {
    const nominee: INominee = {
      ...new Nominee(),
      employeeId: this.selectedEmployeeId,
      nomineeType: this.nomineeType,
    };
    if (this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
      this.nomineeService.getRemainingPercentage(nominee).subscribe(res => {
        this.totalConsumedPercentage = 100 - res;
      });
    }
  }

  trackNomineeId(index: number, item: INominee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  ///     Nominee Form Logic and Validation   ///

  onIdentityChange(): void {
    this.selectedIdentityType = this.editForm.get(['identityType'])!.value;
    this.editForm.get(['idNumber'])!.reset();
    this.editForm.get(['idNumber'])!.updateValueAndValidity();
    this.updateValidationOnNomineeIdentity();
  }

  onChangeIdentityNumberValidation(): void {
    const idNumber = this.editForm.get(['idNumber'])!.value;
    const identityType: IdentityType = this.editForm.get(['identityType'])!.value;
    if (idNumber && identityType) {
      if (identityType === IdentityType.NID) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.isValidNID(idNumber);
      } else if (identityType === IdentityType.PASSPORT) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.isValidPassport(idNumber);
      } else if (identityType === IdentityType.BIRTH_REGISTRATION) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.isValidBRN(idNumber);
      } else if (identityType === IdentityType.OTHER) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.otherIDValidation(idNumber);
      }
    } else {
      this.nomineeIDNumberValidation = { isValid: true, validationMsg: '' };
    }
  }

  updateValidationOnNomineeIdentity(): void {
    const identityType = this.editForm.get(['identityType'])!.value;

    if (identityType === IdentityType.OTHER) {
      this.editForm.get('documentName')!.setValidators([Validators.required, Validators.minLength(0), Validators.maxLength(255)]);
    } else {
      this.editForm.get('documentName')!.clearValidators();
    }
    this.editForm.get('documentName')!.updateValueAndValidity();
  }

  onChangeDateOfBirth($event: any): void {
    const dateOfBirth = this.editForm.get('dateOfBirth')!.value;
    if (dateOfBirth) {
      this.nomineeAge = this.nomineeService.calculateAge(dateOfBirth);
      if (this.nomineeAge < 18 && this.nomineeAge >= 0) {
        this.showGuardianInputFields = true;
      } else {
        this.showGuardianInputFields = false;
      }
      this.editForm.get('age')!.setValue(this.nomineeAge);

      const identityType = this.editForm.get('identityType')!.value;
      const idNumber = this.editForm.get('idNumber')!.value;

      if (identityType === IdentityType.NID && this.nomineeAge < 18) {
        this.editForm.get('identityType')!.reset();
        this.editForm.get('idNumber')!.reset();
      }

      this.updateValidationForGuardian();
    } else {
      this.editForm.get('age')!.setValue(0);
      this.showGuardianInputFields = false;
    }
  }

  isValidIdentityType(identityType: IdentityType): boolean {
    return (
      identityType === IdentityType.NID ||
      identityType === IdentityType.BIRTH_REGISTRATION ||
      identityType === IdentityType.PASSPORT ||
      identityType === IdentityType.OTHER
    );
  }

  shouldShowNomineeIdNumberField(): boolean {
    const nomineeIdentityType = this.editForm.get('identityType')!.value;
    return this.isValidIdentityType(nomineeIdentityType);
  }

  clearForm(): void {
    this.editForm.reset();

    this.loadUsedSharePercentage();

    this.showGuardianInputFields = false;
  }

  ///      Update            ///

  updateForm(nominee: INominee): void {
    this.nomineeFormService.resetForm(this.editForm, nominee);
  }

  onClickPopulateForm(nominee: INominee): void {
    if (nominee.id) {
      const nomineeId = nominee.id;
      this.editForm.reset();
      this.nomineeService.findCommon(nomineeId).subscribe(res => {
        this.updateForm(res.body!);
        this.loadSavedImage(nomineeId);
      });
    }
  }

  ///      Navigation        ///

  redirectToNomineeReport(): void {
    if (this.accessGroup === ACCESS_GROUP_EMPLOYEE && this.nomineeType === NomineeType.GRATUITY_FUND) {
      this.loadUsedSharePercentage();

      if (this.totalConsumedPercentage !== 100) {
        swalOnRejectionForNomineeReport();
      } else {
        this.router.navigate(['/nominee/gf-form/nominee-report', this.selectedEmployeeId]);
      }
    } else if (this.accessGroup === ACCESS_GROUP_EMPLOYEE && this.nomineeType === NomineeType.GENERAL) {
      this.loadUsedSharePercentage();

      if (this.totalConsumedPercentage !== 100) {
        swalOnRejectionForNomineeReport();
      } else {
        this.router.navigate(['/nominee/general-form/nominee-report', this.selectedEmployeeId]);
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  onClickRedirectToViewPage(nominee: INominee): void {
    if (nominee.id) {
      this.router.navigate(['/nominee/gf/' + nominee.id + '/view']);
    }
  }

  ///      Save              ///

  preValidation(): void {
    const nominee = this.createFromForm();
    this.nomineeValidationService.queryForPreValidateNomineeForAdmin(nominee).subscribe(res => {
      this.alertForPreValidation(res.body!);
    });
  }

  alertForPreValidation(nomineeValidation: INomineeValidation): void {
    if (nomineeValidation.doesSharePercentageExceed) {
      const userInputSharePercentage = Number(this.editForm.get('sharePercentage')!.value);
      const remaining = nomineeValidation.remainingSharePercentage!;
      if (userInputSharePercentage > remaining) {
        this.nomineeValidationService.swalValidationMsg("Total share can't be more than 100%. Remaining only " + remaining + '% Share!');
      }
    } else {
      this.save();
    }
  }

  save(): void {
    this.isSaving = true;
    swalOnLoading('Verifying & Saving..');
    const nominee = this.createFromForm();
    if (nominee.id !== undefined && nominee.id !== null) {
      if (this.nomineeImage) {
        this.subscribeToSaveResponse(this.nomineeService.updateWithFileHR(this.nomineeImage, nominee));
      } else {
        this.subscribeToSaveResponse(this.nomineeService.updateHR(nominee));
      }
    } else {
      this.subscribeToSaveResponse(this.nomineeService.createHR(this.nomineeImage, nominee));
    }
  }

  private createFromForm(): INominee {
    return this.nomineeFormService.getNominee(this.editForm);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INominee>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      res => this.onSaveError(res)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    swalOnSavedSuccess();
    this.loadNomineeList();

    this.clearForm();
    this.showGuardianInputFields = false;
  }

  protected onSaveError(res: any): void {
    this.isSaving = false;
    swalOnRequestErrorWithBackEndErrorTitle(res.error.title);
    if (res.error.errorKey === 'nidVerificationFailed' || res.error.errorKey === 'guardianNidVerificationFailed') {
      this.clearForm();
    }
  }

  ///      Delete              ///

  delete(nominee: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        if (this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
          this.nomineeService.deleteCommon(nominee.id).subscribe(
            () => {
              swalOnDeleteSuccess();
              this.loadNomineeList();
              this.clearForm();
            },
            () => this.requestFailed()
          );
        }
      }
    });
  }

  requestFailed(): void {
    swalOnRequestError();
  }

  ///      Nominee Image Logic              ///

  onChangeSelectNomineeImage(event: any): void {
    /* validation: [file type: {image/jpeg}, file size: 2MB,] */
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      const file = event.target.files[0];

      if (!file || file.type !== 'image/jpeg') {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Select JPG/JPEG image only';
        return;
      }
      const sizeInKB = Number(file.size / 1024);
      if (sizeInKB > 2048) {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Max allowed file size is 2MB';
        return;
      }
      this.nomineeImage = file;
      this.isNomineeImageMissing = false;
    }
    return;
  }

  loadSavedImage(id: number): void {
    this.urlForNomineeImg = this.getNomineeImage(id);
    this.changeNomineeImage = false;
    this.isNomineeImageMissing = false;
    this.inValidFile = false;
  }

  getNomineeImage(nomineeId: number): string {
    const url = SERVER_API_URL + 'files/common/nominee-image/' + nomineeId;
    return url;
  }

  changeToReUpload(): void {
    this.changeNomineeImage = true;
    this.isNomineeImageMissing = true;
  }

  ///      Guardian Logic and Validation     ///

  shouldShowGuardianIdNumberField(): boolean {
    const guardianIdentityType = this.editForm.get('guardianIdentityType')!.value;
    return this.isValidIdentityType(guardianIdentityType);
  }

  onGuardianIdentityChange(): void {
    this.selectedGuardianIdentityType = this.editForm.get(['guardianIdentityType'])!.value;
    this.editForm.get(['guardianIdNumber'])!.reset();
    this.updateValidationOnGuardianIdentityType();
  }

  updateValidationOnGuardianIdentityType(): void {
    const identityType = this.editForm.get(['guardianIdentityType'])!.value;

    if (identityType === IdentityType.OTHER) {
      this.editForm.get('guardianDocumentName')!.setValidators([Validators.required, Validators.minLength(0), Validators.maxLength(255)]);
    } else {
      this.editForm.get('guardianDocumentName')!.clearValidators();
    }
    this.editForm.get('guardianDocumentName')!.updateValueAndValidity();
  }

  checkGuardianAge(): void {
    if (dayjs(this.editForm.get('guardianDateOfBirth')!.value).isAfter(dayjs())) {
      this.invalidGuardianAge = true;
    } else {
      this.invalidGuardianAge = false;
    }
  }

  onChangeGuardianIdentityNumberValidation(): void {
    const idNumber = this.editForm.get(['guardianIdNumber'])!.value;
    const identityType: IdentityType = this.editForm.get(['guardianIdentityType'])!.value;
    if (idNumber && identityType) {
      if (identityType === IdentityType.NID) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.isValidNID(idNumber);
      } else if (identityType === IdentityType.PASSPORT) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.isValidPassport(idNumber);
      } else if (identityType === IdentityType.BIRTH_REGISTRATION) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.isValidBRN(idNumber);
      } else if (identityType === IdentityType.OTHER) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.otherIDValidation(idNumber);
      }
    } else {
      this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };
    }
  }

  updateValidationForGuardian(): void {
    const guardianNameFormControl = this.editForm.get('guardianName');
    const guardianFatherNameFormControl = this.editForm.get('guardianFatherName');
    const guardianSpouseNameFormControl = this.editForm.get('guardianSpouseName');
    const guardianDateOfBirthFormControl = this.editForm.get('guardianDateOfBirth');
    const guardianPresentAddressFormControl = this.editForm.get('guardianPresentAddress');
    const guardianPermanentAddressFormControl = this.editForm.get('guardianPermanentAddress');
    const guardianIdentityTypeFormControl = this.editForm.get('guardianIdentityType');
    const guardianDocumentNameFormControl = this.editForm.get('guardianDocumentName');
    const guardianIdNumberFormControl = this.editForm.get('guardianIdNumber');
    const guardianRelationshipFormControl = this.editForm.get('guardianRelationshipWith');

    const fields = [
      guardianNameFormControl,
      guardianFatherNameFormControl,
      guardianSpouseNameFormControl,
      guardianDateOfBirthFormControl,
      guardianPresentAddressFormControl,
      guardianPermanentAddressFormControl,
      guardianIdentityTypeFormControl,
      guardianDocumentNameFormControl,
      guardianIdNumberFormControl,
      guardianRelationshipFormControl,
    ];

    if (this.nomineeAge < 18) {
      guardianNameFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianDateOfBirthFormControl!.setValidators([Validators.required]);
      guardianPresentAddressFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianPermanentAddressFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianIdentityTypeFormControl!.setValidators([Validators.required]);
      guardianIdNumberFormControl!.setValidators([Validators.required]);
      guardianRelationshipFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      this.onChangeGuardianIdentityNumberValidation();
    } else {
      // reset fields
      fields.forEach(x => {
        x!.clearValidators();
        x!.reset();
      });
      this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };
    }

    // update fields value and validity
    fields.forEach(x => {
      x!.updateValueAndValidity();
    });
  }

  // Escape button reset form  ///

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (!this.editForm.get('id')!.value) {
      this.editForm.reset();
    }
  }

  getEmployeeID(event: any): void {
    this.selectedEmployeeId = event.id;
    this.loadNomineeList();
    this.loadUsedSharePercentage();
  }
}
